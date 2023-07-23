import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class User {
    String username;
    String password;
    List<ItemFeed> posts;
    List<User> following;

    public User(String username, String password){
        this.username = username;
        this.password = password;
        this.following = new ArrayList<>();
        this.posts = new ArrayList<>();
    }

    public void addPost(ItemFeed post){
        posts.add(post);
    }

    public void follow(User user){
        following.add(user);
    }

    public List<ItemFeed> getNewsFeed(){
        List<ItemFeed> newsFeed = new ArrayList<>();
        PriorityQueue<ItemFeed> pq = new PriorityQueue<>((a,b)->{
            if(a.getScore()!=b.getScore()){
                return b.getScore()-a.getScore();
            }else{
                return b.getTimestamp().compareTo(a.getTimestamp());
            }
        });

        for(User user: following){
            newsFeed.addAll(user.posts);
        }

        for(ItemFeed ft : newsFeed){
            pq.add(ft);
        }

        List<ItemFeed> sortedList = new ArrayList<>();
        while(!pq.isEmpty()){
            sortedList.add(pq.poll());
        }

        return sortedList;

    }

}
