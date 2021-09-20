package nl.jaapcoomans.demo.testdata.conference.domain;

public class Speaker {
    private final Id speakerId;
    private final String fullName;
    private final String twitterHandle;
    private final String bio;

    private Speaker(Id speakerId, String fullName, String twitterHandle, String bio) {
        this.speakerId = speakerId;
        this.fullName = fullName;
        this.twitterHandle = twitterHandle;
        this.bio = bio;
    }

    public static Speaker create(String emailAddress, String fullName, String twitterHandle, String bio) {
        validateEmailAddress(emailAddress);
        return new Speaker(
                new Id(emailAddress),
                fullName,
                twitterHandle,
                bio
        );
    }

    private static void validateEmailAddress(String emailAddress) {
        if (!emailAddress.contains("@")) {
            throw new IllegalArgumentException("'" + emailAddress + "' is not a valid email address");
        }
    }

    public Id getSpeakerId() {
        return speakerId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getTwitterHandle() {
        return twitterHandle;
    }

    public String getBio() {
        return bio;
    }

    public static final record Id(String emailAddress) {
    }
}
