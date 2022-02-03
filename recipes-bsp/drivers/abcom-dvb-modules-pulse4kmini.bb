KV = "4.4.35"
SRCDATE = "20220128"

PROVIDES = "virtual/blindscan-dvbs"

require abcom-dvb-modules.inc

SRC_URI[md5sum] = "61dc8185dd1fac4e744c34442a854e18"
SRC_URI[sha256sum] = "d114062599c250ddb0c6cb78fb532f4023120e1b4cd98c0f0bd5711ffcea56c7"

COMPATIBLE_MACHINE = "pulse4kmini"

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
