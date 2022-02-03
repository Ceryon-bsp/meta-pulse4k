KV = "4.4.35"
SRCDATE = "20220128"

PROVIDES = "virtual/blindscan-dvbs"

require abcom-dvb-modules.inc

SRC_URI[md5sum] = "8506aa48b21a10d8dfe09dc0eb189f9f"
SRC_URI[sha256sum] = "01647f76244c874323942fd75a3d8c0276f0de205930cea60ef211004f742d61"

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
