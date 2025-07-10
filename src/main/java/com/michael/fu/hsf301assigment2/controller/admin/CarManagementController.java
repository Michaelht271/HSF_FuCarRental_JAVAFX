package com.michael.fu.hsf301assigment2.controller.admin;

import com.michael.fu.hsf301assigment2.controller.BaseController;
import com.michael.fu.hsf301assigment2.entity.Car;
import com.michael.fu.hsf301assigment2.entity.CarStatus;
import com.michael.fu.hsf301assigment2.entity.Producer;
import com.michael.fu.hsf301assigment2.service.impl.CarService;
import com.michael.fu.hsf301assigment2.service.impl.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/admin/cars")
public class CarManagementController extends BaseController {

    private final CarService carService;
    private final ProducerService producerService;
    private static final String RETURN_LINK = "redirect:/api/v1/admin/cars"; // Đường dẫn tới view danh sách xe
    @Autowired
    public CarManagementController(CarService carService, ProducerService producerService) {
        this.carService = carService;
        this.producerService = producerService;
    }

    @GetMapping(path={"/",""})
    public String cars(Model model) {
        model.addAttribute("cars", carService.findAll());
        model.addAttribute("car", new Car()); // để binding vào form thêm
        model.addAttribute("producers", producerService.findAll());
        model.addAttribute("status", CarStatus.values()); // để binding vào form thêm // để binding vào form thêm

        return "admin/cars"; // đường dẫn tới file này
        // Trả về view admin/cars.html
    }


    @PostMapping("/add")
    public String addCar(@ModelAttribute("car") Car car) {
        logger.info("Adding new car: {}", car);
        Long producerId = car.getProducer().getProducerId();

        // Tìm Producer thực sự từ DB
        Producer managedProducer = producerService.findById(producerId);
        car.setProducer(managedProducer);
        managedProducer.addCar(car);
        carService.save(car); // save to DB
        return RETURN_LINK; // redirect to list
    }


    @PostMapping("update")
    public String updateCar(@ModelAttribute("car") Car car) {
        carService.update(car); // hoặc save() nếu bạn dùng chung add/update
        return RETURN_LINK;
    }

    @GetMapping("/delete/{carId}")
    public String deleteCar(@PathVariable Long carId) {
        logger.info("Deleting car with ID: {}", carId);
        Car car = carService.findById(carId);
        if (car != null) {
            carService.delete(car); // Giả sử bạn có phương thức delete trong service
            logger.info("Car deleted successfully");
        } else {
            logger.warn("Car with ID {} not found", carId);
        }
        return RETURN_LINK; // redirect to list
    }
}
