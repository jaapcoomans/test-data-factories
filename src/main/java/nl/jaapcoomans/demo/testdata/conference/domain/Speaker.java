package nl.jaapcoomans.demo.testdata.conference.domain;

public class Speaker {
    private final String emailAddress;
    private final String fullName;
    private final String twitterHandle;
    private final String bio;

    private Speaker(String emailAddress, String fullName, String twitterHandle, String bio) {
        this.emailAddress = emailAddress;
        this.fullName = fullName;
        this.twitterHandle = twitterHandle;
        this.bio = bio;
    }

    public static Speaker create(String emailAddress, String fullName, String twitterHandle, String bio) {
        return new Speaker(
                emailAddress,
                fullName,
                twitterHandle,
                bio
        );
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getTwitterHandle() {
        return twitterHandle;
    }

    public String getBio() {
        return bio;
    }
}
