rootProject.name = "TRedis"

include(
    "app",
    ":redis:redis-api",
    ":redis:redis-impl"
)