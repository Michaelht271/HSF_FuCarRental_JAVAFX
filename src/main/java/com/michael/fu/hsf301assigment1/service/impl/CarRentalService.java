package com.michael.fu.hsf301assigment1.service.impl;

import com.michael.fu.hsf301assigment1.dto.CarRentalDTO;
import com.michael.fu.hsf301assigment1.entity.*;
import com.michael.fu.hsf301assigment1.repository.CarRentalRepository;
import com.michael.fu.hsf301assigment1.repository.CustomerRepository;
import com.michael.fu.hsf301assigment1.service.BaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@AllArgsConstructor
public class CarRentalService extends BaseService {

    private final CarRentalRepository carRentalRepository;
    private final CustomerRepository customerRepository;
    private final CarService carService;

    public void rentailCar(CarRentalDTO request) {
        logger.info("Bắt đầu xử lý thuê xe: customerEmail={}, carId={}, pickupDate={}, returnDate={}",
                request.getCustomerEmail(), request.getCarId(), request.getPickupDate(), request.getReturnDate());

        // Parse và validate ngày
        LocalDate pickupDate = parseDate(request.getPickupDate(), "Ngày nhận xe");
        LocalDate returnDate = parseDate(request.getReturnDate(), "Ngày trả xe");
        validateDateRange(pickupDate, returnDate);

        // Lấy thông tin khách hàng
        Customer customer = findCustomerByEmail(request.getCustomerEmail());

        // Lấy thông tin xe
        Car car = findCarById(request.getCarId());

        // Kiểm tra xe đang được thuê không
        validateCarAvailability(car, pickupDate);

        // Lưu đơn thuê xe
        CarRental carRental = new CarRental();
        carRental.setCustomer(customer);
        carRental.setCar(car);
        carRental.setRentDate(pickupDate);
        carRental.setReturnDate(returnDate);
        carRental.setRentPrice(request.getRentPrice());
        carRental.setStatus(CarRentalStatus.PENDING);

        carRentalRepository.save(carRental);
        logger.info("Lưu thông tin thuê xe thành công: {}", carRental);
    }

    public List<CarRental> findByCustomer(Customer customer) {
        if (customer == null || customer.getEmail() == null) {
            throw new IllegalArgumentException("Thông tin khách hàng không hợp lệ.");
        }

        Customer existingCustomer = customerRepository.findByEmail(customer.getEmail());
        if (existingCustomer == null) {
            throw new IllegalArgumentException("Không tìm thấy khách hàng.");
        }

        return carRentalRepository.findByCustomer_CustomerId(existingCustomer.getCustomerId());
    }

    public List<CarRental> findAll() {
        logger.info("Lấy tất cả thông tin thuê xe.");
        return carRentalRepository.findAll();
    }

    public void updateStatus(Long customerId, Long carId, String status) {
        Customer customer = customerRepository.findByCustomerId(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Không tìm thấy khách hàng với ID: " + customerId);
        }

        Car car = carService.findById(carId);
        if (car == null) {
            throw new IllegalArgumentException("Không tìm thấy xe với ID: " + carId);
        }

        CarRental carRental = carRentalRepository.findByCustomerAndCar(customer, car);
        if (carRental == null) {
            throw new IllegalArgumentException("Không tìm thấy đơn thuê xe tương ứng.");
        }

        try {
            CarRentalStatus newStatus = CarRentalStatus.valueOf(status.toUpperCase());
            carRental.setStatus(newStatus);
            carRentalRepository.save(carRental);
            logger.info("Cập nhật trạng thái đơn thuê xe thành công: {}", carRental);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Trạng thái không hợp lệ: " + status);
        }
    }

    // ============================
    //      PRIVATE METHODS
    // ============================

    private LocalDate parseDate(String dateStr, String label) {
        try {
            return LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(label + " không đúng định dạng (yyyy-MM-dd).");
        }
    }

    private void validateDateRange(LocalDate pickup, LocalDate returnD) {
        if (pickup.isAfter(returnD)) {
            throw new IllegalArgumentException("Ngày nhận xe phải trước hoặc bằng ngày trả xe.");
        }
    }

    private Customer findCustomerByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email khách hàng không được để trống.");
        }
        Customer customer = customerRepository.findByEmail(email);
        if (customer == null) {
            throw new IllegalArgumentException("Không tìm thấy khách hàng với email: " + email);
        }
        return customer;
    }

    private Car findCarById(Long carId) {
        Car car = carService.findById(carId);
        if (car == null) {
            throw new IllegalArgumentException("Không tìm thấy xe với ID: " + carId);
        }
        return car;
    }

    private void validateCarAvailability(Car car, LocalDate pickupDate) {
        boolean isUnavailable = carRentalRepository.existsByCarAndStatusInAndReturnDateAfter(
                car,
                List.of(CarRentalStatus.PENDING, CarRentalStatus.BOOKED),
                pickupDate
        );
        if (isUnavailable) {
            throw new IllegalStateException("Xe hiện đang được thuê trong khoảng thời gian đã chọn.");
        }
    }
}
