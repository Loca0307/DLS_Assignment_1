val scala3Version = "3.3.6"

lazy val root = project
  .in(file("."))
  .settings(
    developers := List(
      Developer("simone.cotard", "Simone Cotardo", "simone.cotardo@usi.ch", url("https://yourwebsite.com")),
      Developer("luca.beltrami", "Luca Beltrami", "luca.beltrami@usi.ch", url("https://yourwebsite.com"))
      ),
    name := "edsl-assignment-01",
    version := "1.0.0",
        scalacOptions ++= Seq(
      "-Yexplicit-nulls"
    ),
    scalaVersion := scala3Version,
  )
