val tornadoFxVersion: String by project

dependencies {
    implementation(project(":redis:redis-impl"))

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("no.tornado:tornadofx:$tornadoFxVersion")
}

javafx {
    version = "12"
    modules("javafx.controls", "javafx.fxml")
    configuration = "compileOnly"
}