setwd("~/Desktop/GIT/Aggregation-Proxy/Measurements/TimestampComparison")

light = read.table("lightTimestamps.txt")
n = nrow(light)
light.reduced = (light[,1] - min(light[,1]))
light.reduced.sec = light.reduced / 1000
light.reduced.min = light.reduced / 60000


start = n-10
end = n
plot(light.reduced.sec, 1:n, xlim = c(light.reduced.sec[start], light.reduced.sec[end]), ylim = c(start, end))



window = 2000
end = n-50
start = end-window
plot(light.reduced.sec, 1:n, xlim = c(light.reduced.sec[start], light.reduced.sec[end]), ylim = c(start, end))
