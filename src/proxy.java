public class proxy {
    public static void main(String[] arg){
        Server proxy = Server.getInstance();
        if (proxy!=null) System.out.println("Server correctly initialized (@"+proxy.getAddress()+":"+proxy.getPort()+")");
        try {
            while (true){
                try{
                    proxy.openServer();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if(proxy.closeServer()) System.out.println("Server correctly closed (@"+proxy.getAddress()+":"+proxy.getPort()+")");
        }
    }
}