import java.util.ArrayList;
import java.util.List;

//import things i will need for the FIFO algorithm, the QUEUE
import java.util.Deque;
import java.util.ArrayDeque;

/**
 * A Memory object is an abstract representation of physical memory composed of frames containing pages. 
 * 
 * Frames in the Memory object are numbered/indexed from 0. Rather than contain an actual page of bytes, a frame may contain a page number.
 * 
 * Frame numbers and page numbers are abstractions of memory addresses.
 */

public class Memory  {

    private final Integer[] frames;

    /**
     * Create a memory containing the given number of frames and indexed 0 .. (numFrames-1).
     * @param numFrames
     */
    public Memory(final int numFrames) {
        this.frames = new Integer[numFrames];
    }
    
    /**
     * Determine if the given frame is empty.
     * @param frameNumber
     */
    public boolean isEmpty(final int frameNumber) {
        return frames[frameNumber] == null;
    }

    /**
     * 
     * @return number of frames of memory.
     */
    public int size() { return frames.length; }

    /**
     * Obtain the number of the page in the given frame.
     * 
     * @param frameNumber
     * @throws AssertionError if this.isEmpty(frameNumber)
     */
    public int get(final int frameNumber) {
        assert !this.isEmpty(frameNumber);
        return frames[frameNumber];
    }

    /**
     * Put the given page in the given frame.
     * @param frameNumber
     * @param pageNumber
     */
    public void put(final int frameNumber, final int pageNumber) { frames[frameNumber] = pageNumber; }

    /**
     * Find the frame containing pageNumber and replace it with newNumber.
     * 
     * @param pageNumber
     * @param newNumber
     * @throws AssertionError if (!this.contains(pageNumber))
     */
    public void replace(final int pageNumber, final int newNumber) {
        assert this.contains(pageNumber);
        this.put(this.indexOf(pageNumber), newNumber);
    }

    /**
     * Determine if a frame in memory contains the given page.
     * @param pageNumber
     */
    public boolean contains(final int pageNumber) {
        return indexOf(pageNumber) != -1;
    }

    /**
     * Obtain the index of the frame containing the given page.
     * @param pageNumber
     * @return the index of the frame containing the given page, or -1 if not found. 
     */
    public int indexOf(final int pageNumber) {
        for (int i=0; i < frames.length; i++) {
            if (!isEmpty(i) && frames[i] == pageNumber) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Obtain a mutable list of the pages currently in memory.
     */
    public List<Integer> pages() {
        final List<Integer> pages = new ArrayList<Integer>();
        for (Integer i : this.frames) {
            pages.add(i);
        }
        return pages;
    }

    /**
     * Obtain a string representation of the memory (as a sequence of frames).
     */
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(('['));


        if (this.size() > 0) {
            stringBuilder.append(this.toString(0));

            for (int i=1; i < this.size(); i++) {
                stringBuilder.append(", ");
                stringBuilder.append(this.toString(i));
            }
        }

        stringBuilder.append(']');
        return stringBuilder.toString();
    }

    /* ********************** Helper methods ********************* */

    private String toString(final int index) {
        if (this.isEmpty(index)) {
            return "-";
        }
        else {
            return this.frames[index].toString();
        }
    }


}
