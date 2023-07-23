import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static Map<String, User> users = new HashMap<>();
    private static User currentLoggedInUser;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean flag = true;

        while(flag){
            System.out.println("Enter Command");
            String input = sc.nextLine();
            String[] inputs = input.split(" ");
            String command = inputs[0].toLowerCase();
            String username = inputs.length>1? inputs[1].toLowerCase():"";
            String password = inputs.length>2?inputs[2].toLowerCase():"";

            switch (command){
                case "signup":
                    signUp(username,password);
                    break;
                case "login":
                    logIn(username,password);
                    break;
                case "post":
                    String content = inputs.length>3? input.substring(5+username.length()+password.length()):"";
                    post(content);
                    break;
                case "reply":
                    String replyContent = inputs.length>3?input.substring(6+username.length()+password.length()):"";
                    reply(replyContent);
                    break;
                case "follow":
                    follow(username);
                    break;
                case "upvotes":
                    upvote(inputs.length>2 ? Integer.parseInt(inputs[1]):-1);
                    break;
                case "downvotes":
                    downvote(inputs.length>2 ? Integer.parseInt(inputs[1]):-1);
                    break;
                case "shownewsfeed":
                    showNewsFeed();
                    break;
                case "exit":
                    flag = false;
                    break;
                default:
                    System.out.println("Invalid Password.");
                    break;
            }
        }
    }

    private static void signUp(String username, String password){
        if(users.containsKey(username)){
            System.out.println("User already exits.Please use different username");
        }else{
            User newUser = new User(username, password);
            users.put(username,newUser);
            System.out.println("User "+username+" has been successfully registered.");
        }
    }

    private static void logIn(String username, String password){
        if(!users.containsKey(username)){
            System.out.println("Username is not found. Please signup first");
        }else{
            User user = users.get(username);
            if(!user.password.equals(password)){
                System.out.println("Invalid password. Please try again");
            }else{
                currentLoggedInUser=user;
                System.out.println("Logged in as "+username+" .");
            }
        }
    }

    private static void post(String content){
        if(currentLoggedInUser==null){
            System.out.println("Please login first.");
        }else{
            ItemFeed newPost = new ItemFeed(currentLoggedInUser.posts.size()+1, currentLoggedInUser.username,content);
            currentLoggedInUser.addPost(newPost);
            System.out.println("Post successfully added.");
        }
    }

    private static void follow(String username){
        if(currentLoggedInUser==null){
            System.out.println("Please login first.");
        }else if(!users.containsKey(username)){
            System.out.println("User "+username+" not found.");
        }else{
            User user = users.get(username);
            currentLoggedInUser.follow(user);
            System.out.println("You are now following "+ username+" .");
        }
    }

    private static void reply(String replyContent){
        if(currentLoggedInUser==null){
            System.out.println("Please login first.");
        }else{
            Scanner scanner=new Scanner(System.in);
            System.out.println("Enter the post Id to reply to: ");
            int postId = scanner.nextInt();
            if(postId<=0 || postId>currentLoggedInUser.posts.size()){
                System.out.println("Invalid post Id");
            }else{
                ItemFeed post = currentLoggedInUser.posts.get(postId-1);
                ItemFeed reply = new ItemFeed(postId*100+post.comments.size()+1,currentLoggedInUser.username, replyContent);
                post.addComment(reply);
                System.out.println("Reply added Successfully");
            }
        }
    }

    private static void upvote(int postId) {
        if (currentLoggedInUser == null) {
            System.out.println("Please login first.");
        } else if (postId <= 0 || postId > currentLoggedInUser.posts.size()) {
            System.out.println("Invalid post ID.");
        } else {
            ItemFeed post = currentLoggedInUser.posts.get(postId - 1);
            post.addUpvotes();
            System.out.println("Post upvoted successfully.");
        }
    }

    private static void downvote(int postId) {
        if (currentLoggedInUser == null) {
            System.out.println("Please login first.");
        } else if (postId <= 0 || postId > currentLoggedInUser.posts.size()) {
            System.out.println("Invalid post ID.");
        } else {
            ItemFeed post = currentLoggedInUser.posts.get(postId - 1);
            post.addDownvotes();
            System.out.println("Post downvoted successfully.");
        }
    }

    private static void showNewsFeed() {
        if (currentLoggedInUser == null) {
            System.out.println("Please login first.");
        } else {
            List<ItemFeed> newsFeed = currentLoggedInUser.getNewsFeed();
            System.out.println("Your News Feed:");
            for (ItemFeed item : newsFeed) {
                String timeAgo = getTimeAgo(item.getTimestamp());
                System.out.println("[" + timeAgo + "] " + item.author + ": " + item.content);
                for (ItemFeed comment : item.comments) {
                    String commentTimeAgo = getTimeAgo(comment.getTimestamp());
                    System.out.println("\t[" + commentTimeAgo + "] " + comment.author + ": " + comment.content);
                }
            }
        }
    }

    private static String getTimeAgo(Instant timestamp) {
        Duration duration = Duration.between(timestamp, Instant.now());
        long seconds = duration.getSeconds();

        if (seconds < 60) {
            return seconds + "s ago";
        } else if (seconds < 3600) {
            long minutes = seconds / 60;
            return minutes + "m ago";
        } else if (seconds < 86400) {
            long hours = seconds / 3600;
            return hours + "h ago";
        } else {
            long days = seconds / 86400;
            return days + "d ago";
        }
    }

}

