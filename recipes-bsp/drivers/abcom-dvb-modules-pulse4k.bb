KV = "4.4.35"
SRCDATE = "20210324"

PROVIDES = "virtual/blindscan-dvbs"

require abcom-dvb-modules.inc

SRC_URI[md5sum] = "f7dee919454c1bec210a01a7de53279c"
SRC_URI[sha256sum] = "8ff87b605a19e6a96c449a5d4c2357f4a42182ae3dd5583dd7abca9fa8d76e6b"

COMPATIBLE_MACHINE = "pulse4k"

INITSCRIPT_NAME = "suspend"
INITSCRIPT_PARAMS = "start 89 0 ."
inherit update-rc.d

do_configure[noexec] = "1"

# Generate a simplistic standard init script
do_compile_append () {
	cat > suspend << EOF
#!/bin/sh

runlevel=runlevel | cut -d' ' -f2

if [ "\$runlevel" != "0" ] ; then
	exit 0
fi

mount -t sysfs sys /sys

/usr/bin/turnoff_power
EOF
}

do_install_append() {
	install -d ${D}${sysconfdir}/init.d
	install -d ${D}${bindir}
	install -m 0755 ${S}/suspend ${D}${sysconfdir}/init.d
	install -m 0755 ${S}/turnoff_power ${D}${bindir}
}

do_package_qa() {
}

FILES_${PN} += " ${bindir} ${sysconfdir}/init.d"

INSANE_SKIP_${PN} += "already-stripped"
