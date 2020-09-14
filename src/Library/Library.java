package Library;

import java.util.ArrayList;

public class Library {
    protected static ArrayList<Book>storage;
    protected int size;

   public Library(){
        storage = new ArrayList<>();
        size=10;
        for (int i = 0; i < size; i++) {
            storage.add(new Book(i));
        }
    }

    public ArrayList<Integer> listBook(){
        ArrayList<Integer> bookId=new ArrayList<>();
        for(int i=0;i<storage.size();i++){
            bookId.add(storage.get(i).id);
        }
        return bookId;
    }

    public synchronized ArrayList<Integer> borrowBook(int bookId){
        if(bookId>=0 && bookId<storage.size()) {

           /* for (Library.Book book : storage) {
                if (book.id == bookId) {
                    storage.remove(book);

                }
            }*/

           for(int i=0;i<storage.size();i++){
               if(storage.get(i).id==bookId){
                   storage.remove(storage.get(i));
               }
           }
        }
        return listBook();
    }

    public synchronized ArrayList<Integer> returnBook(int bookId){
        if(bookId<0 || bookId>=size){
        }else {

            /*for(Library.Book book:storage) {
                if(book.id==bookId) {
                }else{
                    storage.add(new Library.Book(bookId));
                }
            }*/

            boolean flag=true;
            for(int i=0;i<storage.size();i++){
                if(storage.get(i).id==bookId){
                    flag=false;
                }
            }
            if(flag==true){
                storage.add(new Book(bookId));
            }
        }
        return listBook();
    }
}
