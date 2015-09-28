package eu.bose.soundtouch.mdns;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.model.message.header.STAllHeader;
import org.fourthline.cling.model.message.header.UDNHeader;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.types.UDN;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.registry.RegistryListener;

import javax.jmdns.*;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Class description.
 *
 * @author js828576
 * @since 9/26/2015
 */
public class Discoverer3 {

    private static final String MEDIA_RENDERER = "urn:schemas-upnp-org:device:MediaRenderer:1";
    private static final String MEDIA_SERVER = "urn:schemas-upnp-org:device:MediaServer:1";

    public static void main(String[] args) throws Exception {
        RegistryListener listener = new RegistryListener() {

            @Override
            public void remoteDeviceDiscoveryStarted(Registry registry, RemoteDevice device) {
                System.out.println(
                        "Discovery started: " + device.getDisplayString()
                );
            }

            @Override
            public void remoteDeviceDiscoveryFailed(Registry registry, RemoteDevice device, Exception e) {
                System.out.println(
                        "Discovery failed: " + device.getDisplayString() + " => " + e
                );
            }

            @Override
            public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
                if (MEDIA_RENDERER.equals(device.getType().toString())) {
                    System.out.println(
                            "Remote device available: " + device.getDisplayString() + " @ "
                    );
                } else {
                    System.out.println(
                            "Remote device available: " + device.getDisplayString()
                    );
                }
            }

            @Override
            public void remoteDeviceUpdated(Registry registry, RemoteDevice device) {
                System.out.println(
                        "Remote device updated: " + device.getDisplayString()
                );
            }

            @Override
            public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
                System.out.println(
                        "Remote device removed: " + device.getDisplayString()
                );
            }

            @Override
            public void localDeviceAdded(Registry registry, LocalDevice device) {
                System.out.println(
                        "Local device added: " + device.getDisplayString()
                );
            }

            @Override
            public void localDeviceRemoved(Registry registry, LocalDevice device) {
                System.out.println(
                        "Local device removed: " + device.getDisplayString()
                );
            }

            @Override
            public void beforeShutdown(Registry registry) {
                System.out.println(
                        "Before shutdown, the registry has devices: "
                                + registry.getDevices().size()
                );
            }

            @Override
            public void afterShutdown() {
                System.out.println("Shutdown of registry complete!");
            }

        };

        // This will create necessary network resources for UPnP right away
        System.out.println("Starting Cling...");
        UpnpService upnpService = new UpnpServiceImpl(listener);

        // Send a search message to all devices and services, they should respond soon
        upnpService.getControlPoint().search(new STAllHeader());

        // Send a search message to a specific UDN device, it should respond soon
        // UDN udn = UDN.valueOf(MEDIA_RENDERER);
        // upnpService.getControlPoint().search(new UDNHeader(udn));

        // Let's wait 10 seconds for them to respond
        System.out.println("Waiting 10 seconds before shutting down...");
        Thread.sleep(10000);

        // Release all resources and advertise BYEBYE to other UPnP devices
        System.out.println("Stopping Cling...");
        upnpService.shutdown();
    }

}
