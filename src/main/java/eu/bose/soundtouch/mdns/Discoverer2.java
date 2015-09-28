package eu.bose.soundtouch.mdns;

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
public class Discoverer2 {

    private static final String type = "_soundtouch._tcp.local";

    private static JmmDNS jmmDNS;
    private static ServiceListener listener;

    private static boolean isDiscovered = Boolean.FALSE;

    public static void main(String[] args) throws Exception {
        System.out.println("=== Start SoundTouch Discovery ===");
        startDiscovery();

        while (!isDiscovered) {
            // waiting ...
        }
        System.out.println("=== Stop SoundTouch Discovery ===");
    }

    static void startDiscovery() throws IOException {
        listener = new SoundTouchServiceListener();

        jmmDNS = JmmDNS.Factory.getInstance();
        jmmDNS.addNetworkTopologyListener(new SoundTouchNetworkTopologyListner());

    }

    static class SoundTouchNetworkTopologyListner implements NetworkTopologyListener {

        private Map<JmDNS, InetAddress> interfaces = new HashMap<JmDNS, InetAddress>();

        @Override
        public void inetAddressAdded(NetworkTopologyEvent event) {
            InetAddress address = event.getInetAddress();

            JmDNS mdns = event.getDNS();
            mdns.addServiceListener(type, listener);

            interfaces.put(mdns, address);
        }

        @Override
        public void inetAddressRemoved(NetworkTopologyEvent event) {
            JmDNS mdns = event.getDNS();
            mdns.removeServiceListener(type, listener);
            mdns.unregisterAllServices();

            interfaces.remove(mdns);
        }

    }

    static class SoundTouchServiceListener implements ServiceListener {

        @Override
        public void serviceAdded(ServiceEvent serviceEvent) {
            jmmDNS.requestServiceInfo(serviceEvent.getType(), serviceEvent.getName(), 1);
        }

        @Override
        public void serviceRemoved(ServiceEvent serviceEvent) {
            System.out.println("Service removed: " + serviceEvent.getName());
        }

        @Override
        public void serviceResolved(ServiceEvent serviceEvent) {
            System.out.println("Service resolved: " + serviceEvent.getInfo().getQualifiedName() + ", port: " + serviceEvent.getInfo().getPort());
        }

    }

}
