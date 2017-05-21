/**
 *  Test app
 *
 */


// Automatically generated. Make future change here.
definition(
    namespace: "",
    author: "Tracy Hale",
    description: "This is just a test app for me to play with. It'll probably do many different things over time as I try new things out. For personal use only.",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")

preferences {
	section("When this switch is pushed") {
		input "switchButton", "capability.switch", title: "Button"
	}
    section {
			input "sonos", "capability.musicPlayer", title: "On this Sonos player", required: true
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
	// TODO: subscribe to attributes, devices, locations, etc.
	subscribe(switchButton, "switch.on", appHandler)


}

def appHandler(evt) {
    log.debug "I got the time: ${now()}"
    Date currentDateTime = new Date()
    def currentTime = currentDateTime.format('h:mm a',TimeZone.getTimeZone('GMT-7')).toString()
    log.debug "Which is datetime: ${currentTime}"
    state.sound = textToSpeech(currentDateTime.format('h:mm a',TimeZone.getTimeZone('GMT-7')).toString(), true)
    sonos.playTrack(state.sound.uri)
}