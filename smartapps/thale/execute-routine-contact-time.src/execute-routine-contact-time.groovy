/**
 *  Execute Routine (contact/time)
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
    name: "Execute Routine (contact/time)",
    namespace: "thale",
    author: "Tracy Hale",
    description: "This will change to a new mode based on the time of day and if a contact sensor is open or shut",
    category: "My Apps",
    iconUrl: "https://graph.api.smartthings.com/api/devices/icons/st.custom.wu1.nt_clear",
    iconX2Url: "https://graph.api.smartthings.com/api/devices/icons/st.custom.wu1.nt_clear?displaySize=2x",
    iconX3Url: "https://graph.api.smartthings.com/api/devices/icons/st.custom.wu1.nt_clear?displaySize=2x")


preferences {
        page(name: "selectRoutine")
}

def selectRoutine() {
    dynamicPage(name: "selectRoutine", title: "", install: true, uninstall: true) {	
        section("Select the triggers...") {
            input "sensorOpen", "capability.contactSensor", title: "This sensor opens", required: false
            input "sensorClose", "capability.contactSensor", title: "This sensor closes", required: false
            input "timeStart", "time", title: "And it's after this time:"
            input "timeEnd", "time", title: "But it's before this time:"
        }
        def phrases = location.helloHome?.getPhrases()*.label
        if (phrases) {
            phrases.sort()
            section("...to run routines") {
                log.trace phrases
                input "HHPhrase", "enum", title: "Enter Routine", required: true, options: phrases, refreshAfterSelection:true
            }
        }
        section {
        	label(title: "Label this SmartApp", required: false, defaultValue: "")
      	}
    }
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
	subscribe(sensorOpen, "contact.open", contactOpenHandler)
    subscribe(sensorClose, "contact.closed", contactClosedHandler)
}

def contactOpenHandler(evt) {
	log.debug "contact $evt.value"
    
    checkTime()
}

def contactClosedHandler(evt) {
	log.debug "contact $evt.value"
    
    checkTime()
}

def checkTime() {
	log.debug "Checking time and performing actions"
    
	// if between the start and end time, send the correct Hello Home Phrase
    if(timeOfDayIsBetween(timeStart, timeEnd, (new Date()), location.timeZone))
    {
        // after the time and the trigger went; talk to house
        location.helloHome.execute(settings.HHPhrase)
        log.debug "sent $HHPhrase"
    }
}

