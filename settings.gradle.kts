rootProject.name = "TRedis"

include(
    "app",
    "ui-components",
    ":redis:redis-api",
    ":redis:redis-impl"
)