package hr.ferit.dominradic.aplikacija

data class User(
    var firstName: String? = null,
    var lastName: String? = null,
    var username: String? = null,
    var email: String? = null,
    var followingCount: String?=null,
    var followersCount: String?=null,
    var picture: String="https://hips.hearstapps.com/digitalspyuk.cdnds.net/17/13/1490989538-egg.jpg?resize=768:*",
)


