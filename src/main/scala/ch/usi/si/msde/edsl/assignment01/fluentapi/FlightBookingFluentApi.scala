package ch.usi.si.msde.edsl.assignment01.fluentapi

import ch.usi.si.msde.edsl.assignment01.model.* 
import java.time.LocalDate
import java.awt.print.Book

trait NLPFluentApi:

  def named(name: String) = name  // Only for beautifying the API

  
  // Airline
  def airline(name: String)(code: IATACode): Airline = ???
  def `with IATA code`(value: String): IATACode = ???

  // Airport
  def airport(name: String)(code: AirportCode)(city: AirportCity): Airport = ???  // OR NESTED def airport(name: String)(code: AirportCode)(city: AirportCity): Airport = ???
  def `with airport code`(value: String): AirportCode = ???
  def `located in`(value: String): AirportCity = ???

  // Passenger
  def `named as`(first: String)(last: String): PassengerName = ???
  def childPassenger(name: PassengerName) = this
  def youngPassenger(name: PassengerName) = this
  def adultPassenger(name: PassengerName) = this
  def seniorPassenger(name: PassengerName) = this
  def `years old`(age: Int) = this
  def `owns frequent flyer number`(airline: Airline)(code: Int): Passenger = ??? // Int instead of FlyerCode to simplify function call
  def `without frequent flyer number`(): Passenger = ???

  // Fligth
  def pureFlight = this
  def codeshareFlight = this
  def `on date`(onDate: LocalDate) = this
  def from(departure: Airport) = this
  def to(arrival: Airport) = this
  def `sold by`(airline: Airline) = this                            // Optional if flight is pure
  def `with seller flight number`(sellerNumber: Int) = this         // Optional if flight is pure
  def `operated by`(airline: Airline) = this
  def `with operator flight number`(operatorNumber: Int) = this     // Int instead of FlightCode to simplify function call
  def `offers special meals`(meals: SpecialMeal*) = this
  def `includes cabins`(cabins: Cabin*): Flight = ???

  // Cabins/classes
  def economyClass(seats: Set[Seat]): Cabin = ???
  def businessClass(seats: Set[Seat]): Cabin = ???
  def firstClass(seats: Set[Seat]): Cabin = ???
  def `has seats`(seats: Seat*): Set[Seat] = ???

  def seat(letter: Char)(number: Int): Seat = ???

  def medicalMeal(name: String)(iata: SpecialMealIataCode): SpecialMeal = ???
  def religiousMeal(name: String)(iata: SpecialMealIataCode): SpecialMeal = ???
  def childrenMeal(name: String)(iata: SpecialMealIataCode): SpecialMeal = ???

  def `with meal IATA code`(value: String): SpecialMealIataCode = ???

  // Reservation
  def `reserved for`(passenger: Passenger) = this
  def `on flight`(flight: Flight) = this
  def `at seat`(seat: Seat): FlightReservation = ???

  // Trip
  def trip(reservations: FlightReservation*): Trip = ???

  // Special Meal
  def `ordered by`(passenger: Passenger) = this
  def on(reservation: FlightReservation) = this
  def meals(meals: SpecialMeal*): MealOrder = ???

  // Booking
  def booking(code: BookingCode) = this
  def `with booking code`(value: String): BookingCode = ???
  def `involves passengers`(passengers: Passenger*) = this
  def `contains trips`(trips: Trip*): Booking = ???

end NLPFluentApi