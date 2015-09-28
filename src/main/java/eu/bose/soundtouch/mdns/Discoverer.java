package eu.bose.soundtouch.mdns;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;
import java.io.IOException;

/**
 * Class description.
 *
 * @author js828576
 * @since 9/26/2015
 */
public class Discoverer {

    private static final String type = "_soundtouch._tcp.local";

    private static JmDNS jmDNS;
    private static ServiceListener listener;

    public static void main(String[] args) throws Exception {
        System.out.println("=== Start SoundTouch Discovery ===");
        startDiscovery();
    }

    static void startDiscovery() throws IOException {
        jmDNS = JmDNS.create();
        jmDNS.addServiceListener(type, listener = new ServiceListener() {

            @Override
            public void serviceAdded(ServiceEvent serviceEvent) {
                jmDNS.requestServiceInfo(serviceEvent.getType(), serviceEvent.getName(), 1);
            }

            @Override
            public void serviceRemoved(ServiceEvent serviceEvent) {
                System.out.println("Service removed: " + serviceEvent.getName());
            }

            @Override
            public void serviceResolved(ServiceEvent serviceEvent) {
                System.out.println("Service resolved: " + serviceEvent.getInfo().getQualifiedName() + ", port: " + serviceEvent.getInfo().getPort());
            }

        });
    }

}
