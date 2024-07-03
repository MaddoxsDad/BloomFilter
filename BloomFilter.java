import java.util.BitSet;
import java.util.Random;
import java.util.function.Function;

public class BloomFilter<T> {
    private BitSet bitSet;
    private int bitSetSize;
    private int[] hashSeeds;
    private int numberOfHashFunctions;
    private Function<T, Integer>[] hashFunctions;

    @SuppressWarnings("unchecked")
        public BloomFilter(int bitSetSize, int numberOfHashFunctions) {
            this.bitSetSize = bitSetSize;
            this.numberOfHashFunctions = numberOfHashFunctions;
            this.bitSet = new BitSet(bitSetSize);
            this.hashSeeds = new int[numberOfHashFunctions];
            this.hashFunctions = new Function[numberOfHashFunctions];
            Random rand = new Random();

            // Initialize hash functions with random seeds
            for (int i = 0; i < numberOfHashFunctions; i++) {
                final int index = i;
                hashSeeds[i] = rand.nextInt();
                hashFunctions[i] = (T item) -> (item.hashCode() ^ hashSeeds[index]) % bitSetSize;
            }
        }
    public void add(T item) {
        for (Function<T, Integer> hashFunction : hashFunctions) {
            int hash = Math.abs(hashFunction.apply(item));
            bitSet.set(hash);
        }
    }

    public boolean mightContain(T item) {
        for (Function<T, Integer> hashFunction : hashFunctions) {
            int hash = Math.abs(hashFunction.apply(item));
            if (!bitSet.get(hash)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        BloomFilter<String> bloomFilter = new BloomFilter<>(1000, 3);
        bloomFilter.add("apple");
        bloomFilter.add("banana");

        System.out.println(bloomFilter.mightContain("apple"));  // true
        System.out.println(bloomFilter.mightContain("banana")); // true
        System.out.println(bloomFilter.mightContain("cherry")); // false (most likely)
    }
}
