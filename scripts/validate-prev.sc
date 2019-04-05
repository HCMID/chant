import edu.holycross.shot.mid.validator._

val repo = EditorsRepo(".")
val midValidator = Validator(repo)
val reporter = ValidationReporter(midValidator)



def validate(uString : String) = {
  reporter.validate(uString)
}

println("\n\nValidate DSE relations for a given page:")
println("\n\tvalidate(PAGEURN)\n\n")
