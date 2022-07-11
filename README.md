## 만들면서 겪은 문제
File class에서 file.delete()는 예외를 던져주지 않아서 실패를 하였을때 자세한 정보를 얻기 힘들다.
java.nio.file에 있는 Files.delete(Path ..)를 해주면 예외를 반환을 해주어서 실패 원인을 정확하게 파악할수 있다.

```java
try {
   Files.delete(path);
   return true;
    }catch (IOException e){
      e.printStackTrace();
      return false;
  }
```

참고 링크 : https://stackoverflow.com/questions/15336565/java-file-delete-returns-false-but-file-exists-returns-true
