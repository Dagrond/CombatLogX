repositories {
    maven("https://repo.essentialsx.net/releases/")
}

dependencies {
    compileOnly("net.essentialsx:EssentialsX:2.20.1") {
        exclude("io.papermc")
    }
}
