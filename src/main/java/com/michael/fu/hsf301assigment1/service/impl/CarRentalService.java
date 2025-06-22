package com.michael.fu.hsf301assigment1.service.impl;

import com.michael.fu.hsf301assigment1.entity.*;
import com.michael.fu.hsf301assigment1.repository.CarRentalRepository;
import com.michael.fu.hsf301assigment1.repository.CustomerRepository;
import com.michael.fu.hsf301assigment1.service.BaseService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class CarRentalService extends BaseService {
    private final CarRentalRepository carRentalRepository;
   private final CustomerRepository customerRepository ;

    public void rentailCar(Customer customer, Car car, String pickupDate, String returnDate) {
        logger.info("Bắt đầu xử lý thuê xe: customerId={}, carId={}, pickupDate={}, returnDate={}",
                customer.getCustomerId(), car.getCarId(), pickupDate, returnDate);

        // Tạo khóa chính phức hợp
        CarCustomerID id = new CarCustomerID();
        id.setCarId(car.getCarId());
        id.setCustomerId(customer.getCustomerId());

        // Kiểm tra xem đã tồn tại đơn thuê chưa
        if (carRentalRepository.existsById(id)) {
            logger.warn("Đã tồn tại đơn thuê xe với id: {}", id);
            throw new IllegalStateException("Bạn đã đặt xe này rồi.");
        }

        // Tạo đơn thuê mới
        CarRental carRental = new CarRental();
        carRental.setId(id);
        carRental.setCustomer(customer);
        carRental.setCar(car);
        carRental.setRentDate(LocalDate.parse(pickupDate));
        carRental.setReturnDate(LocalDate.parse(returnDate));
        carRental.setStatus(CarRentalStatus.BOOKED);

        logger.debug("Tạo đối tượng CarRental: {}", carRental);

        // Không thêm vào danh sách để tránh lỗi session trùng đối tượng
        // Chỉ lưu carRental, nếu Cascade.ALL thì sẽ lưu liên kết

        carRentalRepository.save(carRental);
        logger.info("Lưu thông tin thuê xe thành công.");
    }

    public List<CarRental> findByCustomer(Customer customer) {

        Customer existingCustomer = customerRepository.findByEmail(customer.getEmail());
        return carRentalRepository.findByCustomer_CustomerId(existingCustomer.getCustomerId());
    }
}
