sys.props.get("plugin.version") match {
  case Some(x) => addSbtPlugin("com.jacoby6000.sbt" % "sbt-pom-build" % x)
  case _ =>
    sys.error(
      """|The system property 'plugin.version' is not defined.
                         |Specify this property using the scriptedLaunchOpts -D.""".stripMargin
    )
}
