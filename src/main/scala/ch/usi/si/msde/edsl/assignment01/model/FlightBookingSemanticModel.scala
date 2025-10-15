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

case class FirstName(val value: String):
  require(value.nonEmpty, "First name cannot be empty")

case class LastName(val value: String):
  require(value.nonEmpty, "Last name cannot be empty")

case class Name(val fn: FirstName, val ln: LastName):
  val fullName: String = fn.value + " " + ln.value

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
  val cabins: Set[Cabin]
  val mealOffers: Set[SpecialMeal]

  require(departure != arrival, "Departure and arrival airports must be different")
  require(sellerFlightNumber.airline == soldBy, "Seller flight number must belong to soldBy airline")
  require(operatorFlightNumber.airline == operatedBy, "Operator flight number must belong to operatedBy airline")

case class PureFlight(
  date: LocalDate,
  departure: Airport,
  arrival: Airport,
  operatedBy: Airline,
  soldBy: Airline,
  sellerFlightNumber: FlightNumber,
  operatorFlightNumber: FlightNumber,
  cabins: Set[Cabin],
  mealOffers: Set[SpecialMeal]
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
  cabins: Set[Cabin],
  mealOffers: Set[SpecialMeal]
) extends Flight:
  require(operatedBy != soldBy, "A CodeshareFlight must be sold and operated by two different airlines")
  require(sellerFlightNumber.code != operatorFlightNumber.code, "A CodeshareFlight must have different 4-digit flight numbers")

case class SpecialMealIataCode(val value: String):
  require(value.matches("[A-Z]{4}"))

sealed trait SpecialMeal:
  val iata: SpecialMealIataCode
  val name: String

case class MedicalDietarySpecialMeal(
  iata: SpecialMealIataCode,
  name: String
) extends SpecialMeal

case class ReligiousMeal(
  iata: SpecialMealIataCode,
  name: String
) extends SpecialMeal

case class ChildrenMeal(
  iata: SpecialMealIataCode,
  name: String
) extends SpecialMeal

case class MealOrder(meals: Set[SpecialMeal], flightReservation: FlightReservation):
  require(meals.nonEmpty, "At least one meal must exists")

  val hasChildrenMeal = meals.exists(_.isInstanceOf[ChildrenMeal])
  require(!hasChildrenMeal || flightReservation.passenger.isInstanceOf[ChildPassenger], "A ChildrenMeal can be requested only by a ChildrenPassenger")

sealed trait Cabin:
  val seats: Set[Seat]

  require(seats.nonEmpty)

case class EconomyClass(
  seats: Set[Seat]
) extends Cabin

case class PremiumEconomyClass(
  seats: Set[Seat]
) extends Cabin

case class BusinessClass(
  seats: Set[Seat]
) extends Cabin

case class FirstClass(
  seats: Set[Seat]
) extends Cabin

case class SeatNumber(val value: Int):
  require(value > 0 && value <=99)

  def formatted: String = f"$value%02d"

case class SeatLetter(val value: Char):
  require(value.isUpper)

  def string: String = value.toString()

case class Seat(val letter: SeatLetter, val number: SeatNumber):
  def formatted: String = letter.string + number.formatted;

sealed trait Passenger:
  val name: Name
  val age: Int
  val ffn: Option[FrequentFlyerNumber]

  require(age > 0 && age < 100)

case class ChildPassenger(
  name: Name,
  age: Int,
  ffn: Option[FrequentFlyerNumber]
) extends Passenger:
  require(age <= 5)

case class YoungPassenger(
  name: Name,
  age: Int,
  ffn: Option[FrequentFlyerNumber]
) extends Passenger:
  require(age > 5 && age <= 12)

case class AdultPassenger(
  name: Name,
  age: Int,
  ffn: Option[FrequentFlyerNumber]
) extends Passenger:
  require(age > 12 && age <= 60)

case class SeniorPassenger(
  name: Name,
  age: Int,
  ffn: Option[FrequentFlyerNumber]
) extends Passenger:
  require(age > 60)

case class Booking(val code: BookingCode, val trips: Set[Trip], val passengers: Set[Passenger]):
  require(trips.nonEmpty && passengers.nonEmpty)

case class FlightReservation(val passenger: Passenger, val seat: Seat, val flight: Flight):
  require(flight.cabins.exists(_.seats.contains(seat)), "Seat must exist in one of the flight cabins")  // Anonymous funct.: cabin => cabin.seats.contains(seat)

case class Trip(val flights: Set[Flight], val reservations: Set[FlightReservation]):
  require(flights.nonEmpty, "A trip must have at least one flight")
  require(reservations.nonEmpty, "A trip must have at least one reservation")
  require(reservations.forall(r => flights.contains(r.flight)), "All reservations must refer to flights in this trip")



/** Exercise 2: Example 1 
  */
@main def runExample1(): Unit =
  //TODO: da semplificare per allineare alle richieste dell'assignment
  val syd = Airport(
    AirportCode("SYD"),
    AirportCity("Sydney"),
    "Sydney Airport"
  )
  val cdg = Airport(
    AirportCode("CDG"),
    AirportCity("Paris"),
    "Paris Airport"
    )
  val sin = Airport(
    AirportCode("SIN"),
    AirportCity("Singapore"),
    "Singapore Airport"
    )

  val ox = Airline(
    "Oceanic Airlines", 
    IATACode("OX")
    )
  val pa = Airline(
    "Pan Am", 
    IATACode("PA")
    )

  val alex = ChildPassenger(
    name = Name(FirstName("Alex"), LastName("Geek")),
    age = 4,
    ffn = Some(FrequentFlyerNumber(ox, FrequentFlyerCode(89156273)))
  )
  val john = AdultPassenger(
    name = Name(FirstName("John"), LastName("Doe")),
    age = 30,
    ffn = None
  )

  // Same seats reused in all flights
  val sA1 = Seat(SeatLetter('A'), SeatNumber(1))
  val sA2 = Seat(SeatLetter('A'), SeatNumber(2))
  val econ = EconomyClass(Set(sA1, sA2))
  val busi = BusinessClass(Set(sA1))

  // --- Special meals offerti sul volo
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
    departure = syd,
    arrival = cdg,
    operatedBy = ox,
    soldBy = ox,
    sellerFlightNumber = FlightNumber(FlightCode(815), ox),
    operatorFlightNumber = FlightNumber(FlightCode(815), ox),
    cabins = Set(econ, busi),
    mealOffers = Set(childMeal, gfMeal)
  )

  // SYD -> SIN, pure (OX0700)
  val fBack1 = PureFlight(
    date = LocalDate.of(2026, 10, 15),
    departure = syd,
    arrival = sin,
    operatedBy = ox,
    soldBy = ox,
    sellerFlightNumber = FlightNumber(FlightCode(700), ox),
    operatorFlightNumber = FlightNumber(FlightCode(700), ox),
    cabins = Set(econ),
    mealOffers = Set(childMeal, gfMeal)
  )

  // SIN -> CDG, codeshare (sold OX0403, operated PA0100)
  val fBack2 = CodeshareFlight(
    date = LocalDate.of(2026, 10, 15),
    departure = sin,
    arrival = cdg,
    operatedBy = pa,
    soldBy = ox,
    sellerFlightNumber = FlightNumber(FlightCode(403), ox),   // OX0403
    operatorFlightNumber = FlightNumber(FlightCode(100), pa), // PA0100
    cabins = Set(econ),
    mealOffers = Set(childMeal, gfMeal)
  )

  val resAlexOut = FlightReservation(alex, sA1, fOut)
  val resJohnBack = FlightReservation(john, sA2, fBack1)

  val orderAlex = MealOrder(Set(childMeal), resAlexOut) // OK
  val orderJohn = MealOrder(Set(gfMeal), resJohnBack)  // OK  

  val tripOut = Trip(flights = Set(fOut), reservations = Set(resAlexOut))
  val tripBack = Trip(flights = Set(fBack1, fBack2), reservations = Set(resJohnBack))

  // --- Booking (passegners in reservations ⊆ passengers)
  val booking = Booking(
    code = BookingCode("A1B2C3"),
    trips = Set(tripOut, tripBack),
    passengers = Set(alex, john)
  )

  println(printBooking(booking))
  println()
  println(printOrders(Seq(orderAlex, orderJohn)))

def printOrders(orders: Iterable[MealOrder]): String =
  val lines = orders.map { o =>
    val pas = o.flightReservation.passenger.name.fullName
    val meals = o.meals.map(_.name).mkString(", ")
    val date = o.flightReservation.flight.date
    val op = o.flightReservation.flight.operatedBy.name
    val opNum = o.flightReservation.flight.operatorFlightNumber.value
    s"  - $pas: $meals on flight [$opNum] operated by $op (on $date)"
  }.mkString("\n")

  s"Orders:\n$lines"

def printBooking(b: Booking): String =
  var text = s"Booking ${b.code.value}\n"

  text += "Passengers:\n"
  for p <- b.passengers do
    val ffnText = p.ffn.map(_.value).getOrElse("No Frequent Flyer Number")
    text += s"  - ${p.name.fullName} (FFN: $ffnText)\n"

  var i = 1
  for trip <- b.trips do
    text += s"Trip $i:\n"
    for f <- trip.flights do text += s"  ${printFlightLine(f)}\n"
    i += 1

  text

def printFlightLine(f: Flight): String =
  val dep = f.departure.code.value
  val arr = f.arrival.code.value
  val date = f.date
  val soldBy = f.soldBy.name
  val operatedBy = f.operatedBy.name
  val soldCode = f.sellerFlightNumber.value
  val operatedCode = f.operatorFlightNumber.value

  f match
    case _: PureFlight =>
      s"$date - $dep -> $arr - [Pure flight] operated by $operatedBy [$operatedCode]"
    case _: CodeshareFlight =>
      s"$date - $dep -> $arr - [Codeshare flight] sold by $soldBy [$soldCode] and operated by $operatedBy [$operatedCode]"


// --------- MINI ESEMPIO DI ERRORE (ChildrenMeal a un adulto) ---------
/*@main def runExampleFailChildrenMeal(): Unit =
  val mxp = Airport(AirportCode("MXP"), AirportCity("Milan"), "Milano Malpensa")
  val zrh = Airport(AirportCode("ZRH"), AirportCity("Zurich"), "Zurich Airport")
  val lx  = Airline("Swiss", IATACode("LX"))

  val adult = AdultPassenger(Name(FirstName("Luca"), LastName("Rossi")), age = 35, ffn = None)

  val econ = EconomyClass(Set(Seat(SeatLetter('A'), SeatNumber(1))))
  val chml = ChildrenMeal(SpecialMealIataCode("CHML"), "Children Meal")

  val date: LocalDate = LocalDate.of(2025, 3, 10)

  val f = PureFlight(
    date = date,
    departure = mxp,
    arrival = zrh,
    operatedBy = lx,
    soldBy = lx,
    sellerFlightNumber   = FlightNumber(FlightCode(123), lx),
    operatorFlightNumber = FlightNumber(FlightCode(123), lx),
    cabins = Set(econ),
    mealOffers = Set(chml)
  )

  try
    val badOrder = MealOrder(Set(chml), adult, f)
    println("ERRORE: non avrei dovuto arrivare qui " + badOrder)
  catch
    case ex: IllegalArgumentException =>
      println("OK: vincolo rispettato → " + ex.getMessage)*/