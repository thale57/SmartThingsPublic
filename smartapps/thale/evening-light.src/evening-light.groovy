/**
 *  Evening Light
 *
 *  Copyright 2014 Tracy Hale
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "Evening Light",
    namespace: "thale",
    author: "Tracy Hale",
    description: "This will allow me to set up lights that will turn on for a specified amount of time each evening after sundown. An optional offset allows the time to be skewed from the actual sunset, while still being driven by sundown.",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/ModeMagic/rise-and-shine.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/ModeMagic/rise-and-shine@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/ModeMagic/rise-and-shine@2x.png")



preferences {
	section("At sundown...") {
		input "sunsetOn", "capability.switch", title: "Turn on the selected devices", required: true, multiple: true
        input "minutesToKeepOn", "number", title: "Keep the devices on for X minutes", required: true
	}
    
    section ("Sunset offset (optional)...") {
		input "sunsetOffsetValue", "text", title: "HH:MM", required: false
		input "sunsetOffsetDir", "enum", title: "Before or After", required: false, options: ["Before","After"]
	}
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated(settings) {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
	subscribe(location, "position", locationPositionChange)
	subscribe(location, "sunsetTime", sunsetTimeHandler)
	
	astroCheck()
}

def locationPositionChange(evt) {
	log.trace "locationChange()"
	astroCheck()
}

def sunsetTimeHandler(evt) {
	log.trace "sunsetTimeHandler()"
	astroCheck()
}

def astroCheck() {
	def s = getSunriseAndSunset(zipCode: zipCode, sunriseOffset: sunriseOffset, sunsetOffset: sunsetOffset)

	def now = new Date()
	def setTime = s.sunset
	log.debug "setTime: $setTime"
	
	if (state.setTime != setTime.time) {
		state.setTime = setTime.time
		unschedule("sunsetHandler")

	    if(setTime.before(now)) {
	        setTime.next()
	    }
	    log.info "scheduling sunset handler for $setTime"
	    runDaily(setTime, sunsetHandler)
	}
}


def sunsetHandler() {
	log.info "Executing sunset handler"
	if (sunsetOn) {
		sunsetOn.on()
        def keepOn = 60 * minutesToKeepOn
        runIn(keepOn, turnOffDevices)
	}
    
}

def turnOffDevices() {
	log.info "Turning off devices now"
    sunsetOn.off()
}

private getSunsetOffset() {
	sunsetOffsetValue ? (sunsetOffsetDir == "Before" ? "-$sunsetOffsetValue" : sunsetOffsetValue) : null
}
