setwd("~/Desktop/GIT/Aggregation-Proxy/Measurements/TimestampComparison")

light = read.table("./timestamps/lightTimestamps-7.txt")
light.n = nrow(light)

battery = read.table("./timestamps/batteryTimestamps-7.txt")
battery.n = nrow(battery)

noise = read.table("./timestamps/noiseTimestamps-7.txt")
noise.n = nrow(noise)

# Plot realtime

par(mfrow=c(2, 1))

plot(light[,1], rep(3, light.n), pch = 20, col = adjustcolor('green', alpha.f = 0.2), ylim = c(0, 5))
points(battery[,1], rep(2, battery.n), pch = 20, col = adjustcolor('red', alpha.f = 0.2))
points(noise[,1], rep(1, noise.n), pch = 20, col = adjustcolor('blue', alpha.f = 0.2))

plot(light, pch = 20, col = adjustcolor('green', alpha.f = 0.7) , type = 'l')
points(battery, pch = 20, col = adjustcolor('red', alpha.f = 0.7), type = 'l')
points(noise, pch = 20, col = adjustcolor('blue', alpha.f = 0.7), type = 'l')



