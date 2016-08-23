with open('output') as file:
    context = file.readlines()

lightFile = open('lightTimestamps.txt','w')
noiseFile = open('noiseTimestamps.txt', 'w')
batteryFile = open('batteryTimestamps.txt', 'w')

for line in context:
	if "TIMESTAMP-LIGHT" in line:
		split = line.split()
		lightFile.write(split[-1] + '\n')
	elif "TIMESTAMP-NOISE" in line:
		split = line.split()
		noiseFile.write(split[-1] + '\n')
	elif "TIMESTAMP-BATTERY" in line:
		split = line.split()
		batteryFile.write(split[-1] + '\n')

lightFile.close()
noiseFile.close()
batteryFile.close()

