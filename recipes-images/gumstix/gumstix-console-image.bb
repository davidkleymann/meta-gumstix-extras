DESCRIPTION = "A basic console image for Gumstix boards."
LICENSE = "MIT"

IMAGE_FEATURES += "splash ssh-server-openssh"
IMAGE_FEATURES += "package-management debug-tweaks"
# Uncomment below to include dev tools and packages
# IMAGE_FEATURES += "tools-sdk dev-pkgs"

IMAGE_LINGUAS = "en-us"

inherit core-image

# Gumstix machines individually RDEPEND on the firware they need but we repeat
# it here as we might want to use the same image on multiple different machines.
FIRMWARE_INSTALL = " \
    linux-firmware-sd8686 \
    linux-firmware-sd8787 \
    linux-firmware-wl18xx \
    wl18xx-fw \
"

SYSTEM_TOOLS_INSTALL = " \
    alsa-utils \
    bluez-alsa \
    cpufrequtils \
    systemd-analyze \
    tzdata \
    rtk-hciattach \
"

DEV_TOOLS_INSTALL = " \
    e2fsprogs-resize2fs \
    memtester \
    mtd-utils-ubifs \
    u-boot-mkimage \
"

NETWORK_TOOLS_INSTALL = " \
    curl \
    dnsmasq \
    hostapd \
    ethtool \
    iproute2 \
    iputils \
    iw \
    ntp \
"

MEDIA_TOOLS_INSTALL = " \
    media-ctl \
    raw2rgbpnm \
    v4l-utils \
    yavta \
"

GRAPHICS_LIBS = " \
    mtdev \ 
    tslib \
"

UTILITIES_INSTALL = " \
    coreutils \
    diffutils \
    dtc \
    findutils \
    gpsd \
    grep \
    gzip \
    less \
    nano \
    packagegroup-cli-tools \
    packagegroup-cli-tools-debug \
    sudo \
    tar \
    vim \
    wget \
    zip \
    python \
    python3 \
    python3-pip \
    bzip2 \
    git \
    git-perltools \
    resize-rootfs \
"

GSTREAMER_INSTALL = " \
    gstreamer1.0 \
    gstreamer1.0-libav \
    gstreamer1.0-rtsp-server \
    gstreamer1.0-meta-base \
    gstreamer1.0-plugins-good \
    gstreamer1.0-plugins-bad \
    gstreamer1.0-plugins-base \
    gstreamer1.0-plugins-ugly \
"

IMAGE_INSTALL += " \
    ${FIRMWARE_INSTALL} \
    ${SYSTEM_TOOLS_INSTALL} \
    ${DEV_TOOLS_INSTALL} \
    ${NETWORK_TOOLS_INSTALL} \
    ${MEDIA_TOOLS_INSTALL} \
    ${GRAPHICS_LIBS} \
    ${UTILITIES_INSTALL} \
    ${GSTREAMER_INSTALL} \
"

IMAGE_INSTALL_append_rpi += " \
    dpkg \
    apt \
    userland \
    python3-rpi-ws281x \
"

IMAGE_INSTALL_append_tegra += " \
    kernel-modules \
    gstreamer1.0-plugins-tegra \
    gstreamer1.0-plugins-nvvideo4linux2 \
    gstreamer1.0-omx-tegra \
    gstreamer1.0-plugins-nveglgles \
    gstreamer1.0-plugins-nvarguscamerasrc \
    gstreamer1.0-plugins-nvv4l2camerasrc \
    gstreamer1.0-plugins-nvvideosinks \
    gstreamer1.0-plugins-nvjpeg \
    nvgstapps \
    tegra-tools \
    nvgstcapture \
    cuda-toolkit \
    cudnn \
    tensorrt \
    protobuf \
    hdf5 \
    python3 \
    python3-numpy \
    python3-keras-applications \
    python3-keras-preprocessing \
    python3-pip \
    python3-wheel \
    python3-protobuf \
    python3-grpcio \
    python3-absl \
    python3-astor \
    python3-gast \
    python3-termcolor \
    python3-h5py \
    python3-pyparsing \
    python3-matplotlib \
    python3-kiwisolver \
    python3-pillow \
    opencv \
"

ROOTFS_CMD ?= ""
ROOTFS_POSTPROCESS_COMMAND += "${ROOTFS_CMD}"

# To speed up boot time for devices that do not have a wireless
# network adapter on-board by default, remove the wpa service
# for the wireless adapter
remove_wpa() {
    rm -rf ${IMAGE_ROOTFS}/etc/systemd/system/multi-user.target.wants/wpa_supplicant@wlan0.service
}

# Create a generic 'gumstix' user account, part of the gumstix group,
# using '/bin/sh' and with a home directory '/home/gumstix' (see
# /etc/default/useradd).  We set the password to 'gumstix' and add them
# to the 'sudo' group.
inherit extrausers
EXTRA_USERS_PARAMS = " \
    useradd -P gumstix -G sudo gumstix; \
    usermod -P gumstix root; \
"

update_sudoers(){
    sed -i 's/# %sudo/%sudo/' ${IMAGE_ROOTFS}/etc/sudoers
}

ROOTFS_POSTPROCESS_COMMAND += "update_sudoers;"
