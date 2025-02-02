DESCRIPTION = "The Gumstix LXQT image.  This provides simple desktop \
environment using X11."
LICENSE = "MIT"

IMAGE_FEATURES += "x11-base"

require gumstix-console-image.bb

IMAGE_INSTALL += " \
    gnome-bluetooth \
    man \
    man-pages \
    lxqt-connman-applet \
    packagegroup-lxqt-base \
    psplash \
    lxdm-theme \
    chromium-x11 \
"

# Network Manager manages WPA supplicant---we don't need an interface-specific
# systemd service in this case.
zap_wlan0_wpa() {
    rm -f ${IMAGE_ROOTFS}/etc/systemd/system/multi-user.target.wants/wpa_supplicant@wlan0.service
}

ROOTFS_POSTPROCESS_COMMAND =+ "zap_wlan0_wpa;"
