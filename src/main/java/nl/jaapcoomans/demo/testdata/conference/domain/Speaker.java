package nl.jaapcoomans.demo.testdata.conference.domain;

public class Speaker {
    public static final int MAX_BIO_LENGTH = 500;

    private final Id speakerId;
    private String fullName;
    private String twitterHandle;
    private String bio;

    private Speaker(Id speakerId, String fullName, String twitterHandle, String bio) {
        this.speakerId = speakerId;
        this.fullName = fullName;
        this.twitterHandle = twitterHandle;
        this.bio = bio;
    }

    public static Speaker create(String emailAddress, String fullName, String twitterHandle, String bio) {
        validateEmailAddress(emailAddress);
        validateTwitterHandle(twitterHandle);
        validateBio(bio);
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

    public void updateDetails(String fullName, String twitterHandle) {
        validateTwitterHandle(twitterHandle);
        this.fullName = fullName;
        this.twitterHandle = twitterHandle;
    }

    private static void validateTwitterHandle(String twitterHandle) {
        if (twitterHandle != null && !twitterHandle.isEmpty()) {
            if (!twitterHandle.matches("@[A-Za-z0-9_]{15}")) {
                throw new IllegalArgumentException("'" + twitterHandle + "' is not a valid Twitter handle");
            }
        }
    }

    public void updateBio(String bio) {
        validateBio(bio);
        this.bio = bio;
    }

    private static void validateBio(String bio) {
        if (bio != null && !bio.isEmpty()) {
            if (bio.length() > MAX_BIO_LENGTH) {
                throw new IllegalArgumentException("Bio is too long. Can only contain " + MAX_BIO_LENGTH + " characters.");
            }
        }
    }

    public boolean isProfileComplete() {
        return isNameFilled() && isBioFilled();
    }

    public boolean isNameFilled() {
        return fullName != null && !fullName.isEmpty();
    }

    public boolean isBioFilled() {
        return bio != null && !bio.isEmpty();
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
