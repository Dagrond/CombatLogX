repositories {
    maven("https://nexus.sirblobman.xyz/private/") {
        credentials {
            username = rootProject.ext.get("mavenUsername") as String
            password = rootProject.ext.get("mavenPassword") as String
        }
    }
}

dependencies {
    compileOnly("com.mvdw-software:FeatherBoard:5.3.0")
}
