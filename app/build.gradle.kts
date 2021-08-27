val tornadoFxVersion: String by project

plugins {
    id("org.springframework.boot") version "2.5.4"
}

apply(plugin = "application")

dependencies {
    implementation(project(":ui-components"))
    implementation(project(":redis:redis-impl"))

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("no.tornado:tornadofx:$tornadoFxVersion")
}

javafx {
    version = "12"
    modules("javafx.controls", "javafx.fxml")
}

tasks.bootJar {
    enabled = true
}
