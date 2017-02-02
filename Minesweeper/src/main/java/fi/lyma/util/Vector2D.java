package fi.lyma.util;

public class Vector2D<T> {
    private T x, y;

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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector2D<?> vector2D = (Vector2D<?>) o;

        if (!x.equals(vector2D.x)) return false;
        return y.equals(vector2D.y);
    }

    @Override
    public int hashCode() {
        int result = x.hashCode();
        result = 31 * result + y.hashCode();
        return result;
    }
}
