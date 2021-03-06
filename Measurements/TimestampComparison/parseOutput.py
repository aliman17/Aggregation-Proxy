#!/usr/bin/python

with open('version') as file:
    version_context = file.readlines()

version = int(version_context[0]) + 1

with open('./outputs/output-'+str(version)) as file:
    context = file.readlines()

lightFile = open('./timestamps/lightTimestamps-'+str(version)+'.txt','w')
noiseFile = open('./timestamps/noiseTimestamps-'+str(version)+'.txt', 'w')
batteryFile = open('./timestamps/batteryTimestamps-'+str(version)+'.txt', 'w')

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

versionFile = open('version', 'w')
versionFile.write(str(version))

lightFile.close()
noiseFile.close()
batteryFile.close()
versionFile.close()

