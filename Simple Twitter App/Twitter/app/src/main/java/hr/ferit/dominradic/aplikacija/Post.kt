package hr.ferit.dominradic.aplikacija

data class Post(
    var tweet: String?=null,
    var name: String?=null,
    var username: String?=null,
    var userEmail: String?=null,
    var userPhoto: String?="https://hips.hearstapps.com/digitalspyuk.cdnds.net/17/13/1490989538-egg.jpg?resize=768:*",
    var datePost:String?=null
)
