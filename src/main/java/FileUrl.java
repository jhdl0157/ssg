public enum FileUrl {
    MAC("/Users/jaeho/Desktop/ssg/src/main/java/data"),WINDOW("D:\\SSG\\src\\main\\java\\data");
    private final String url;
    FileUrl(String url){
        this.url=url;
    }
    public String getUrl(){
        return url;
    }
    public String getMacDirUrl(){return url+"/";}
    public String getWindowDirUrl(){return url+"\\";}
}
