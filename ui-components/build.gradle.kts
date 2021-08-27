val tornadoFxVersion: String by project

dependencies {
    implementation("no.tornado:tornadofx:$tornadoFxVersion")
}

javafx {
    version = "12"
    modules("javafx.controls", "javafx.fxml")
}