package fi.lyma.util;

/**
 * Vector that holds x and y components of the type T.
 *
 * @param <T> Type of the x and y components
 */
public class Vector2D<T> {
    private T x, y;

    /**
     * Constructs {@link Vector2D} with given x and y components.
     *
     * @param x Location of the vector in x-axis
     * @param y Location of the vector in y-axis
     */
    public Vector2D(T x, T y) {
        this.x = x;
        this.y = y;
    }

    public T getX() {
        return x;
    }

    public T getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Vector2D<?> vector2D = (Vector2D<?>) o;

        if (!x.equals(vector2D.x)) {
            return false;
        }
        return y.equals(vector2D.y);
    }

    @Override
    public int hashCode() {
        int result = x.hashCode();
        result = 31 * result + y.hashCode();
        return result;
    }
}
