package ch.usi.si.msde.edsl.assignment01.fluentapi

import ch.usi.si.msde.edsl.assignment01.model.* 
import java.time.LocalDate
import java.awt.print.Book

trait NLPFluentApi:

  def named(name: String) = name  // Only for beautifying the API
  def `flight code`(value: Int): FlightCode = ???
  
  // Airline
  def airline(name: String)(code: IATACode): Airline = ???
  def `with IATA code`(value: String): IATACode = ???

  // Airport
  def airport(name: String)(code: AirportCode)(city: AirportCity): Airport = ???  // OR NESTED def airport(name: String)(code: AirportCode)(city: AirportCity): Airport = ???
  def `with airport code`(value: String): AirportCode = ???
  def `in city`(value: String): AirportCity = ???

  // Passenger
  def passNamed(first: String)(last: String): PassengerName = ???
  def childPassenger(name: PassengerName)(age: Int) = this
  def youngPassenger(name: PassengerName)(age: Int) = this
  def adultPassenger(name: PassengerName)(age: Int) = this
  def seniorPassenger(name: PassengerName)(age: Int) = this
  def withFrequentFlyer(airline: Airline)(code: FrequentFlyerCode): Passenger = ???
  def `ff code`(value: Int): FrequentFlyerCode = ???
  def noFrequentFlyer(): Passenger = ???

  // Fligth
  def `pure flight` = this
  def `codeshare flight` = this
  def `on date`(onDate: LocalDate) = this
  def from(departure: Airport) = this
  def to(arrival: Airport) = this
  def soldBy(airline: Airline) = this                               // Could be optional the flight is pure
  def `with seller flight number`(sellerNumber: FlightCode) = this  // Could be optional the flight is pure
  def operatedBy(airline: Airline) = this
  def `with operator flight number`(operatorNumber: FlightCode) = this 
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
  def reservedFor(passenger: Passenger) = this
  def onFlight(flight: Flight) = this
  def atSeat(seat: Seat): FlightReservation = ???

  // ---------- trips (function sequences) ----------
  /** Build a trip providing a date to materialize flights (if your impl needs it))(
    * and a varargs of flights plus varargs of reservations.
    */
  def trip(reservations: FlightReservation*): Trip = ???

  // ---------- meal orders ----------
  def order(passenger: Passenger) = this
  def on(reservation: FlightReservation) = this
  def meals(meals: SpecialMeal*): MealOrder = ???

  // Booking
  def booking(code: BookingCode) = this
  def `with booking code`(value: String): BookingCode = ???
  def `with passenger`(passengers: Passenger*) = this
  def `contains trips`(trips: Trip*): Booking = ???

end NLPFluentApi