package ch.usi.si.msde.edsl.assignment01.model

/**
  * Types in the JDK to define dates.
  */
import java.time.LocalDate
import java.awt.print.Book
import scala.language.unsafeNulls


/*
 * Implement here, or in other scala files in this package,
 * the semantic model for Exercise 2. If you use other
 * files, please remember to import them.
 */

case class PassengerName(val first: String, val last: String):
  require(first.nonEmpty && last.nonEmpty, "First and last name cannot be empty")
  val fullName: String = first + " " + last

case class FrequentFlyerCode(val value: Int):
  require(value > 0 && value <= 99999999, "Frequent flyer code must be between 1 and 99999999 (max 8 numbers)")

  def formatted: String = f"$value%08d"

case class FrequentFlyerNumber(val airline: Airline, val code: FrequentFlyerCode):
  val value: String = airline.code.value + code.formatted

case class BookingCode(val value: String):
  require(value.matches("[A-Z0-9]{6}"), "Booking code must be exactly 6 uppercase digits (numbers or letters)")

case class AirportCode(value: String):
  require(value.matches("[A-Z]{3}"), "Airport code must be exactly 3 uppercase letters")

case class AirportCity(val value: String):
  require(value.length > 0)

case class Airport(val code: AirportCode, val city: AirportCity, val name: String):
  require(name.nonEmpty)

case class IATACode(val value: String):
  require(value.matches("[A-Z]{2}"), "IATACode must be exactly 2 uppercase letters")

case class FlightCode(val value: Int):
  require(value > 0 && value <= 9999)

  def formatted: String = f"$value%04d"

case class Airline(val name: String, val code: IATACode):
  require(name.nonEmpty)

case class FlightNumber(val code: FlightCode, val airline: Airline):
  val value: String = airline.code.value + code.formatted

sealed trait Flight: 
  val date: LocalDate
  val departure: Airport
  val arrival: Airport
  val operatedBy: Airline
  val soldBy: Airline
  val sellerFlightNumber: FlightNumber
  val operatorFlightNumber: FlightNumber
  val cabins: Set[Cabin]  // Bonus
  val mealOffers: Set[SpecialMeal]  // Bonus

  require(departure != arrival, "Departure and arrival airports must be different")
  require(sellerFlightNumber.airline == soldBy, "Seller airline and soldBy airline must be the same")
  require(operatorFlightNumber.airline == operatedBy, "Operator airline and operatedBy airline must be the same")

case class PureFlight(
  date: LocalDate,
  departure: Airport,
  arrival: Airport,
  operatedBy: Airline,
  soldBy: Airline,
  sellerFlightNumber: FlightNumber,
  operatorFlightNumber: FlightNumber,
  cabins: Set[Cabin], // Bonus
  mealOffers: Set[SpecialMeal]  // Bonus
) extends Flight:
  require(operatedBy == soldBy, "A PureFlight must be operated and sold by the same airline")
  require(sellerFlightNumber == operatorFlightNumber, "A PureFlight must have same seller and operator flight number")

case class CodeshareFlight (
  date: LocalDate,
  departure: Airport,
  arrival: Airport,
  operatedBy: Airline,
  soldBy: Airline,
  sellerFlightNumber: FlightNumber,
  operatorFlightNumber: FlightNumber,
  cabins: Set[Cabin], // Bonus
  mealOffers: Set[SpecialMeal]  // Bonus
) extends Flight:
  require(operatedBy != soldBy, "A CodeshareFlight must be sold and operated by two different airlines")
  require(sellerFlightNumber.code != operatorFlightNumber.code, "A CodeshareFlight must have different 4-digit flight numbers")

// Bonus
case class SpecialMealIataCode(val value: String):
  require(value.matches("[A-Z]{4}"))

// Bonus
sealed trait SpecialMeal:
  val iata: SpecialMealIataCode
  val name: String

// Bonus
case class MedicalDietarySpecialMeal(
  iata: SpecialMealIataCode,
  name: String
) extends SpecialMeal

// Bonus
case class ReligiousMeal(
  iata: SpecialMealIataCode,
  name: String
) extends SpecialMeal

case class ChildrenMeal(
  iata: SpecialMealIataCode,
  name: String
) extends SpecialMeal

// Bonus
case class MealOrder(meals: Set[SpecialMeal], flightReservation: FlightReservation):
  require(meals.nonEmpty, "At least one meal must exists")

  val hasChildrenMeal = meals.exists(_.isInstanceOf[ChildrenMeal])
  require(!hasChildrenMeal || flightReservation.passenger.isInstanceOf[ChildPassenger], "A ChildrenMeal can be requested only by a ChildrenPassenger")

// Bonus
sealed trait Cabin:
  val seats: Set[Seat]

  require(seats.nonEmpty)

// Bonus
case class EconomyClass(
  seats: Set[Seat]
) extends Cabin

// Bonus
case class PremiumEconomyClass(
  seats: Set[Seat]
) extends Cabin

// Bonus
case class BusinessClass(
  seats: Set[Seat]
) extends Cabin

// Bonus
case class FirstClass(
  seats: Set[Seat]
) extends Cabin

// Bonus
case class SeatNumber(val value: Int):
  require(value > 0 && value <=99)

  def formatted: String = f"$value%02d"

// Bonus
case class SeatLetter(val value: Char):
  require(value.isUpper)

  def string: String = value.toString()

// Bonus
case class Seat(val letter: SeatLetter, val number: SeatNumber):
  def formatted: String = letter.string + number.formatted;

sealed trait Passenger:
  val name: PassengerName
  val age: Int  // Bonus
  val ffn: Option[FrequentFlyerNumber]

  require(age > 0 && age < 100) // Bonus

case class ChildPassenger(
  name: PassengerName,
  age: Int, // Bonus
  ffn: Option[FrequentFlyerNumber]
) extends Passenger:
  require(age <= 5) // Bonus

case class YoungPassenger(
  name: PassengerName,
  age: Int, // Bonus
  ffn: Option[FrequentFlyerNumber]
) extends Passenger:
  require(age > 5 && age <= 12) // Bonus

case class AdultPassenger(
  name: PassengerName,
  age: Int, // Bonus
  ffn: Option[FrequentFlyerNumber]
) extends Passenger:
  require(age > 12 && age <= 60)  // Bonus

case class SeniorPassenger(
  name: PassengerName,
  age: Int, // Bonus
  ffn: Option[FrequentFlyerNumber]
) extends Passenger:
  require(age > 60) // Bonus

case class Booking(val code: BookingCode, val trips: Set[Trip], val passengers: Set[Passenger]):
  val passInReservations: Set[Passenger] = trips.flatMap(_.reservations.map(_.passenger)) // Trip1: [R1, R2] Trip2: [R3, R4] -> Set(Set(R1, R2), Set(R3, R4)) -> Set(Set(P1, P2), Set(P3, P4)) -> Set(P1, P2, P3, P4)

  require(trips.nonEmpty && passengers.nonEmpty)
  require(passInReservations.subsetOf(passengers), "All passengers used in trip reservations must belong to the booking")

case class FlightReservation(val passenger: Passenger, val seat: Seat, val flight: Flight):
  require(flight.cabins.exists(_.seats.contains(seat)), "Seat must exist in one of the flight cabins")  // Anonymous funct.: cabin => cabin.seats.contains(seat)

case class Trip(val reservations: Set[FlightReservation]):
  require(reservations.nonEmpty, "A trip must have at least one reservation")

/** Exercise 2: Example 1 
  */
@main def runExample1(): Unit =
  val SYD = Airport(
    AirportCode("SYD"),
    AirportCity("Sydney"),
    "Sydney Airport"
  )
  val CDG = Airport(
    AirportCode("CDG"),
    AirportCity("Paris"),
    "Paris Airport"
    )
  val SIN = Airport(
    AirportCode("SIN"),
    AirportCity("Singapore"),
    "Singapore Airport"
    )

  val OX = Airline(
    "Oceanic Airlines", 
    IATACode("OX")
    )
  val PA = Airline(
    "Pan Am", 
    IATACode("PA")
    )

  val alex = ChildPassenger(
    name = PassengerName("Alex", "Geek"),
    age = 4,
    ffn = Some(FrequentFlyerNumber(OX, FrequentFlyerCode(89156273)))
  )

  val A1 = Seat(SeatLetter('A'), SeatNumber(1))
  val A2 = Seat(SeatLetter('A'), SeatNumber(2))
  val econ = EconomyClass(Set(A1, A2))
  val busi = BusinessClass(Set(A1))

  val childMeal = ChildrenMeal(
    SpecialMealIataCode("BBML"),
    "Baby Meal"
    )
  val gfMeal = MedicalDietarySpecialMeal(
    SpecialMealIataCode("GFML"),
    "Gluten-Free Meal"
    )

  // SYD -> CDG, pure (OX0815)
  val fOut = PureFlight(
    date = LocalDate.of(2025, 10, 1),
    departure = SYD,
    arrival = CDG,
    operatedBy = OX,
    soldBy = OX,
    sellerFlightNumber = FlightNumber(FlightCode(815), OX),
    operatorFlightNumber = FlightNumber(FlightCode(815), OX),
    cabins = Set(econ, busi),
    mealOffers = Set(childMeal, gfMeal)
  )

  // SYD -> SIN, pure (OX0700)
  val fBack1 = PureFlight(
    date = LocalDate.of(2025, 10, 15),
    departure = SYD,
    arrival = SIN,
    operatedBy = OX,
    soldBy = OX,
    sellerFlightNumber = FlightNumber(FlightCode(700), OX),
    operatorFlightNumber = FlightNumber(FlightCode(700), OX),
    cabins = Set(econ),
    mealOffers = Set(childMeal, gfMeal)
  )

  // SIN -> CDG, codeshare (sold OX0403, operated PA0100)
  val fBack2 = CodeshareFlight(
    date = LocalDate.of(2025, 10, 15),
    departure = SIN,
    arrival = CDG,
    operatedBy = PA,
    soldBy = OX,
    sellerFlightNumber = FlightNumber(FlightCode(403), OX),   // OX0403
    operatorFlightNumber = FlightNumber(FlightCode(100), PA), // PA0100
    cabins = Set(econ),
    mealOffers = Set(childMeal, gfMeal)
  )

  val resAlexOut = FlightReservation(alex, A1, fOut)
  val resAlexBack1 = FlightReservation(alex, A2, fBack1)
  val resAlexBack2 = FlightReservation(alex, A1, fBack2)

  val orderAlex = MealOrder(Set(childMeal), resAlexOut) // Allowed

  val tripOut = Trip(reservations = Set(resAlexOut))
  val tripBack = Trip(reservations = Set(resAlexBack1, resAlexBack2))

  val booking = Booking(
    code = BookingCode("A1B2C3"),
    trips = Set(tripOut, tripBack),
    passengers = Set(alex)
  )