package ch.usi.si.msde.edsl.assignment01.fluentapi

import ch.usi.si.msde.edsl.assignment01.model.* 
import java.time.LocalDate

trait NLPFluentApi:

  // BOOKING 

  def booking(code: String)(trips: Trip*)(passengers: Passenger*): Booking =
    Booking(
      BookingCode(code),
      trips.toSet,
      passengers.toSet
    )

  // TRIP

  class TripBuilder:
    def on(date: LocalDate)(flights: BuiltFlight*): Trip =
      val realizedFlights = flights.map(f => f.build(date))
      Trip(realizedFlights.toSet)


  def TripBuilder(): TripBuilder = new TripBuilder

  // FLIGHT 

  // Flight Departure
  class FlightDepartureBuilder:
    def from(departure: Airport): FlightArrivalBuilder =
      FlightArrivalBuilder(departure)

  def FlightBuilder(): FlightDepartureBuilder = new FlightDepartureBuilder

  // Flight Arrival
  case class FlightArrivalBuilder(departure: Airport):
    def to(arrival: Airport): FlightDetailsBuilder =
      require(departure != arrival, "Departure and arrival airports must be different")
      FlightDetailsBuilder(departure, arrival)
  
  // Operator Airline
  case class FlightDetailsBuilder(departure: Airport, arrival: Airport):
    def operatedBy(operator: Airline): FlightSoldByBuilder =
      FlightSoldByBuilder(departure, arrival, operator)

  // Seller Airline
  case class FlightSoldByBuilder(departure: Airport, arrival: Airport, operatedBy: Airline):
    def soldBy(seller: Airline): FlightNumberBuilder =
      FlightNumberBuilder(departure, arrival, operatedBy, seller)

  // Assign flight numbers
  case class FlightNumberBuilder(
    departure: Airport,
    arrival: Airport,
    operatedBy: Airline,
    soldBy: Airline
  ):

    /** Build a PureFlight (same airline selling and operating) */
    def pure(code: Int): BuiltFlight =
      require(operatedBy == soldBy, "PureFlight must have the same seller and operator")
      val fc = FlightCode(code)
      val fn = FlightNumber(fc, operatedBy)
      BuiltFlight((date: LocalDate) =>
        PureFlight(date, departure, arrival, operatedBy, soldBy, fn, fn)
      )

    /** Build a CodeshareFlight (different airlines) */
    def codeshare(sellerCode: Int, operatorCode: Int): BuiltFlight =
      require(operatedBy != soldBy, "CodeshareFlight must have different seller and operator")
      val sellerFN = FlightNumber(FlightCode(sellerCode), soldBy)
      val operatorFN = FlightNumber(FlightCode(operatorCode), operatedBy)
      BuiltFlight((date: LocalDate) =>
        CodeshareFlight(date, departure, arrival, operatedBy, soldBy, sellerFN, operatorFN)
      )


  case class BuiltFlight(build: LocalDate => Flight)

  // PASSENGER 

  class PassengerBuilder:
    def named(fn: String, ln: String): PassengerFFBuilder =
      val name = Name(FirstName(fn), LastName(ln))
      PassengerFFBuilder(name)

  def PassengerBuilder(): PassengerBuilder = new PassengerBuilder

  case class PassengerFFBuilder(name: Name):
    def withFrequentFlyer(airline: Airline, code: Int): Passenger =
      val ffn = FrequentFlyerNumber(airline, FrequentFlyerCode(code))
      Passenger(name, Some(ffn))

    def noFrequentFlyer(): Passenger =
      Passenger(name, None)

end NLPFluentApi