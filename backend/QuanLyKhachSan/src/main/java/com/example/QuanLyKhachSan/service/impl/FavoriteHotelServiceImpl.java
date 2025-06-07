package com.example.QuanLyKhachSan.service.impl;

import com.example.QuanLyKhachSan.entity.Customer;
import com.example.QuanLyKhachSan.entity.FavoriteHotel;
import com.example.QuanLyKhachSan.entity.Hotel;
import com.example.QuanLyKhachSan.entity.User;
import com.example.QuanLyKhachSan.exception.CustomExceptions;
import com.example.QuanLyKhachSan.repository.CustomerRepository;
import com.example.QuanLyKhachSan.repository.FavoriteHotelRepository;
import com.example.QuanLyKhachSan.repository.HotelRepository;
import com.example.QuanLyKhachSan.repository.UserRepository;
import com.example.QuanLyKhachSan.service.FavoriteHotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FavoriteHotelServiceImpl implements FavoriteHotelService {
    @Autowired
    private FavoriteHotelRepository favoriteHotelRepository;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public void addFavoriteHotel(String userId, String hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(
                () -> new CustomExceptions.ResourceNotFoundException("Hotel not found with id: " + hotelId));
        if (hotel == null) {
            throw new CustomExceptions.ResourceNotFoundException("Hotel not found with id: " + hotelId);
        }
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomExceptions.ResourceNotFoundException("User not found with id: " + userId));
        if (user == null) {
            throw new CustomExceptions.ResourceNotFoundException("User not found with id: " + userId);
        }
        Customer customer = customerRepository.findByUserId(user.getUserId())
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException("Customer not found with user id: " + userId));
        if (customer == null) {
            throw new CustomExceptions.ResourceNotFoundException("Customer not found with id: " + userId);
        }
        FavoriteHotel favoriteHotel = favoriteHotelRepository.findByCustomerAndHotel(customer, hotel);
        if (favoriteHotel != null) {
            removeFavoriteHotel(customer.getUser().getUserId(), hotel.getHotelId());
        }
        favoriteHotel = new FavoriteHotel();
        favoriteHotel.setCustomer(customer);
        favoriteHotel.setHotel(hotel);
        favoriteHotel.setCreatedAt(LocalDateTime.now());
        favoriteHotelRepository.save(favoriteHotel);
    }

    @Override
    public void removeFavoriteHotel(String userId, String hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow(
                () -> new CustomExceptions.ResourceNotFoundException("Hotel not found with id: " + hotelId));
        if (hotel == null) {
            throw new CustomExceptions.ResourceNotFoundException("Hotel not found with id: " + hotelId);
        }
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomExceptions.ResourceNotFoundException("User not found with id: " + userId));
        if (user == null) {
            throw new CustomExceptions.ResourceNotFoundException("User not found with id: " + userId);
        }
        Customer customer = customerRepository.findByUserId(user.getUserId()).orElseThrow(
                () -> new CustomExceptions.ResourceNotFoundException("Customer not found with user id: " + userId));
        if (customer == null) {
            throw new CustomExceptions.ResourceNotFoundException("Customer not found with id: " + userId);
        }
        FavoriteHotel favoriteHotel = favoriteHotelRepository.findByCustomerAndHotel(customer, hotel);
        if (favoriteHotel == null) {
            throw new CustomExceptions.ResourceNotFoundException("Favorite hotel not found with id: " + hotelId);
        }
        favoriteHotelRepository.delete(favoriteHotel);
    }

    @Override
    public List<Hotel> getFavoriteHotelsByUserId(String userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomExceptions.ResourceNotFoundException("User not found with id: " + userId));
        if (user == null) {
            throw new CustomExceptions.ResourceNotFoundException("User not found with id: " + userId);
        }
        Customer customer = customerRepository.findByUserId(user.getUserId()).orElseThrow(
                () -> new CustomExceptions.ResourceNotFoundException("Customer not found with user id: " + userId));
        List<FavoriteHotel> favoriteHotels = favoriteHotelRepository.findByCustomer(customer);
        return favoriteHotels.stream().map(FavoriteHotel::getHotel).toList();
    }
}
