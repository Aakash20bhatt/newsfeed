import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

class ItemFeed{

    int id;
    String  author;
    String content;
    int upvotes;
    int downvotes;
    List<ItemFeed> comments;

    Instant timestamp;

    public ItemFeed(int id, String author, String content){
        this.id = id;
        this.author = author;
        this.content = content;
        this.upvotes = 0;
        this.downvotes = 0;
        this.timestamp = Instant.now();
        this.comments = new ArrayList<>();
    }

    public void addComment(ItemFeed comment){
        comments.add(comment);
    }

    public int addUpvotes(){
        return upvotes++;
    }

    public int addDownvotes(){
        return downvotes;
    }

    public int getScore(){
        return upvotes-downvotes;
    }

    public Instant getTimestamp(){
        return timestamp;
    }

}