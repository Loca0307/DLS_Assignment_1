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

case class FrequentFlyerNumber(val airline: Airline, val code: FrequentFlyerCode):  //TODO: Valutare se deve essere associata direttamente la Airline oppure si può associare un 2-digit generico
  val value: String = airline.code.value + code.formatted                           // dall'esempio il FFN usa "OX" quindi uno IATA code della airline quindi per quanto ne sappiamo io rimarrei con lo IATA code. Per me ha senso come lo gestiamo ora 

case class BookingCode(val value: String):
  require(value.matches("[A-Z0-9]{6}"), "Booking code must be exactly 6 uppercase digits (numbers or letters)")

case class AirportCode(value: String):
  require(value.matches("[A-Z]{3}"), "Airport code must be exactly 3 uppercase letters")

case class AirportCity(val value: String):
  require(value.length > 0)

case class Airport(val code: AirportCode, val city: AirportCity, val name: String):
  require(name.length > 0)

case class IATACode(val value: String):
  require(value.matches("[A-Z]{2}"), "IATACode must be exactly 2 uppercase letters")

case class FlightCode(val value: Int):
  require(value > 0 && value < 9999)

  def formatted: String = f"$value%04d"

case class Airline(val name: String, val code: IATACode):
  require(name.length > 0)

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

  require(departure != arrival, "Departure and arrival airports must be different")

case class PureFlight( // Non dovrebbero avere "extends AbstractEvent queste classi?"
  date: LocalDate,
  departure: Airport,
  arrival: Airport,
  operatedBy: Airline,
  soldBy: Airline,
  sellerFlightNumber: FlightNumber,
  operatorFlightNumber: FlightNumber
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
  operatorFlightNumber: FlightNumber
) extends Flight:
  require(operatedBy != soldBy, "A CodeshareFlight must be sold and operated by two different airlines") // TODO: Valutare se mettere il require che il sellerFLightNumber e operatorFlightNumber siano diverse, come abbiamo fatto nel pureFlight. Decidere se e possibile o no che le 2 airline per il volo possono avere lo stesso FLightNumber
  

case class Trip(val flights: Set[Flight]):
  require(flights.size > 0)

case class Passenger(val name: Name, val ffn: Option[FrequentFlyerNumber]) //TODO: Valutare se FrequentFlyerNumber è unico o può averne più di uno, per ogni compagnia aerea
                                                                          // puo` essere effettivamente ha lo IATA code specifico della airline, magari si intende un numero di un passeggiero che viaggia frequentemente con quella airline specifica. Io lo aggiungerei

case class Booking(val code: BookingCode, val trips: Set[Trip], val passengers: Set[Passenger]):
  require(trips.size > 0 && passengers.size > 0)





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
    "Singapore Changi"
    )

  val ox = Airline(
    "Oceanic Airlines",
    IATACode("OX")
    )
  val pa = Airline(
    "Pan Am",
    IATACode("PA")
    )

  val alex = Passenger(
    name = Name(FirstName("Alex"), LastName("Geek")),
    ffn = Some(FrequentFlyerNumber(ox, FrequentFlyerCode(89156273)))  // Some indicates the optional value is specified
  )

  val outDate = LocalDate.of(2025, 12, 27)
  val backDate = LocalDate.of(2026, 1, 15)

  // SYD -> CDG, pure (OX0815)
  val f_out = PureFlight(
    date = outDate,
    departure = syd,
    arrival = cdg,
    operatedBy = ox,
    soldBy = ox,
    sellerFlightNumber = FlightNumber(FlightCode(815), ox),
    operatorFlightNumber = FlightNumber(FlightCode(815), ox)
  )

  // SYD -> SIN, pure (OX0700)
  val f_back1 = PureFlight(
    date = backDate,
    departure = syd,
    arrival = sin,
    operatedBy = ox,
    soldBy = ox,
    sellerFlightNumber = FlightNumber(FlightCode(700), ox),
    operatorFlightNumber = FlightNumber(FlightCode(700), ox)
  )

  // SIN -> CDG, codeshare (sold OX0403, operated PA0100)
  val f_back2 = CodeshareFlight(
    date = backDate,
    departure = sin,
    arrival = cdg,
    operatedBy = pa,
    soldBy = ox,
    sellerFlightNumber = FlightNumber(FlightCode(403), ox),
    operatorFlightNumber = FlightNumber(FlightCode(100), pa)
  )

  val tripOut = Trip(Set(f_out))
  val tripBack = Trip(Set(f_back1, f_back2))

  val booking = Booking(
    code = BookingCode("123456"),
    trips = Set(tripOut, tripBack),
    passengers = Set(alex)
  )
  
  // --- Stampa di riepilogo
  println(printBooking(booking))

def printBooking(b: Booking): String =
  var text = s"Booking ${b.code.value}\n"

  text += "Passengers:\n"
  for p <- b.passengers do
    val ffnText = if p.ffn.isDefined then p.ffn.get.value else "No Frequent Flyer Number"
    text += s"  - ${p.name.fullName} (Frequent Flyer Number: $ffnText)\n"

  var tripNum = 1
  for trip <- b.trips do
    text += s"Trip $tripNum:\n"
    for flight <- trip.flights do
      text += s"  ${printFlight(flight)}\n"
    tripNum += 1

  text

def printFlight(f: Flight): String =
  val dep = f.departure.code.value
  val arr = f.arrival.code.value
  val date = f.date
  val soldBy = f.soldBy.name
  val operatedBy = f.operatedBy.name
  val soldCode = f.sellerFlightNumber.value
  val operatedCode = f.operatorFlightNumber.value

  if f.isInstanceOf[PureFlight] then
    s"$date $dep -> $arr [PURE FLIGHT] - operated by $operatedBy (code $operatedCode)"
  else
    s"$date $dep -> $arr [CODESHARE FLIGHT] - sold by $soldBy (code $soldCode), operated by $operatedBy (code $operatedCode)"

/** Examples to construct dates
  */

/*
@main def datatypeExamples: Unit =
  // 1st of January 2024
  val date1 = LocalDate.of(2024, 1, 1)

  // 31st of may 2021
  val date2 = LocalDate.of(2021, 5, 31)

  println(date1)
  println(date2)

  */