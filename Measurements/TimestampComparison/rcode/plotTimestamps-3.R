setwd("~/Desktop/GIT/Aggregation-Proxy/Measurements/TimestampComparison")

light = read.table("./timestamps/lightTimestamps-3.txt")
light.n = nrow(light)
light.reduced = (light[,1] - min(light[,1]))
light.reduced.sec = light.reduced / 1000
light.reduced.min = light.reduced / 60000

battery = read.table("./timestamps/batteryTimestamps-3.txt")
battery = battery[-1,]
battery.n = length(battery)
battery.reduced = (battery - min(battery))
battery.reduced.sec =battery.reduced / 1000
battery.reduced.min =battery.reduced / 60000


window = 2000
end = light.n
start = 1
plot(light.reduced.sec, 1:light.n, xlim = c(light.reduced.sec[start], light.reduced.sec[end]), ylim = c(start, end), pch = '.')
points(battery.reduced.sec, 1:battery.n, col = 'red', pch = '.')
