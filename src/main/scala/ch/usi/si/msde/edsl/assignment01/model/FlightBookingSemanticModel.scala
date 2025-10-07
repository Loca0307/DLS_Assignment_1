package ch.usi.si.msde.edsl.assignment01.model

/**
  * Types in the JDK to define dates.
  */
import java.time.LocalDate

/*
 * Implement here, or in other scala files in this package,
 * the semantic model for Exercise 2. If you use other
 * files, please remember to import them.
 */

/** Exercise 2: Example 1 
  */
@main def example1Booking: Unit =
  val exampleBooking = ???

  println(exampleBooking)

/** Examples to construct dates
  */
@main def datatypeExamples: Unit =
  // 1st of January 2024
  val date1 = LocalDate.of(2024, 1, 1)

  // 31st of may 2021
  val date2 = LocalDate.of(2021, 5, 31)

  println(date1)
  println(date2)