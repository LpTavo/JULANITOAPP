import java.util.ArrayList;
import java.util.List;

// Clase Producto
class Producto {
    String codigo;
    String nombre;
    String marca;
    String color;
    double precioCompra;
    double precioVenta;
    double porcentajeDescuentoMax;
    double unidadesExistencia;
    String medida;
    String categoria;

    public Producto(String codigo, String nombre, String marca, String color, double precioCompra, double unidadesExistencia) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.marca = marca;
        this.color = color;
        this.precioCompra = precioCompra;
        this.precioVenta = precioCompra * 1.4; // Precio de venta es 40% más que precio de compra
        this.porcentajeDescuentoMax = 20.0; // Porcentaje máximo de descuento
        this.unidadesExistencia = unidadesExistencia;
        this.medida = "unidad"; // Medida por defecto
        this.categoria = "sin_categoria"; // Categoría por defecto
    }

    // Método para actualizar información de producto tras una compra
    public void actualizarDespuesCompra(double cantidadComprada) {
        this.precioCompra = (this.precioCompra * this.unidadesExistencia + precioCompra * cantidadComprada) / (this.unidadesExistencia + cantidadComprada);
        this.precioVenta = this.precioCompra * 1.4;
        this.porcentajeDescuentoMax = 20.0;
        this.unidadesExistencia += cantidadComprada;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", marca='" + marca + '\'' +
                ", color='" + color + '\'' +
                ", precioCompra=" + precioCompra +
                ", precioVenta=" + precioVenta +
                ", porcentajeDescuentoMax=" + porcentajeDescuentoMax +
                ", unidadesExistencia=" + unidadesExistencia +
                ", medida='" + medida + '\'' +
                ", categoria='" + categoria + '\'' +
                '}';
    }
}

// Clase Compra
class Compra {
    int consecutivo;
    String fecha;
    String proveedor;
    String codigoProducto;
    double precioCompra;
    double cantidad;

    public Compra(int consecutivo, String fecha, String proveedor, String codigoProducto, double precioCompra, double cantidad) {
        this.consecutivo = consecutivo;
        this.fecha = fecha;
        this.proveedor = proveedor;
        this.codigoProducto = codigoProducto;
        this.precioCompra = precioCompra;
        this.cantidad = cantidad;
    }

    public double calcularTotal() {
        double subtotal = this.precioCompra * this.cantidad;
        double iva = subtotal * 0.19;
        return subtotal + iva;
    }

    @Override
    public String toString() {
        return "Compra{" +
                "consecutivo=" + consecutivo +
                ", fecha='" + fecha + '\'' +
                ", proveedor='" + proveedor + '\'' +
                ", codigoProducto='" + codigoProducto + '\'' +
                ", precioCompra=" + precioCompra +
                ", cantidad=" + cantidad +
                '}';
    }
}

// Clase Venta
class Venta {
    int consecutivo;
    String fecha;
    String cedulaCliente;
    String medioPago;
    String modalidad;
    List<String> codigosProductos;
    List<Double> cantidades;

    public Venta(int consecutivo, String fecha, String cedulaCliente, String medioPago, String modalidad, List<String> codigosProductos, List<Double> cantidades) {
        this.consecutivo = consecutivo;
        this.fecha = fecha;
        this.cedulaCliente = cedulaCliente;
        this.medioPago = medioPago;
        this.modalidad = modalidad;
        this.codigosProductos = codigosProductos;
        this.cantidades = cantidades;
    }

    public double calcularTotal(List<Producto> productos) {
        double total = 0.0;
        for (int i = 0; i < codigosProductos.size(); i++) {
            String codigo = codigosProductos.get(i);
            double cantidad = cantidades.get(i);
            Producto producto = buscarProductoPorCodigo(productos, codigo);
            if (producto != null) {
                double precioVenta = producto.precioVenta;
                double subtotal = precioVenta * cantidad;
                double iva = subtotal * 0.19;
                total += subtotal + iva;
            }
        }
        return total;
    }

    private Producto buscarProductoPorCodigo(List<Producto> productos, String codigo) {
        for (Producto producto : productos) {
            if (producto.codigo.equals(codigo)) {
                return producto;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Venta{" +
                "consecutivo=" + consecutivo +
                ", fecha='" + fecha + '\'' +
                ", cedulaCliente='" + cedulaCliente + '\'' +
                ", medioPago='" + medioPago + '\'' +
                ", modalidad='" + modalidad + '\'' +
                ", codigosProductos=" + codigosProductos +
                ", cantidades=" + cantidades +
                '}';
    }
}

public class FulanitoSimulacion {

    public static void main(String[] args) {
        // Crear productos
        List<Producto> productos = new ArrayList<>();
        productos.add(new Producto("P001", "Camiseta", "Nike", "Azul", 15.0, 50));
        productos.add(new Producto("P002", "Zapatos", "Adidas", "Negro", 30.0, 30));
        productos.add(new Producto("P003", "Pantalón", "Levis", "Gris", 25.0, 40));

        // Simular compra de productos
        List<Compra> compras = new ArrayList<>();
        compras.add(new Compra(1, "2024-03-19", "Proveedor A", "P001", 15.0, 10));
        compras.add(new Compra(2, "2024-03-20", "Proveedor B", "P002", 30.0, 20));

        // Actualizar información de productos después de la compra
        for (Compra compra : compras) {
            Producto producto = buscarProductoPorCodigo(productos, compra.codigoProducto);
            if (producto != null) {
                producto.actualizarDespuesCompra(compra.cantidad);
            }
        }

        // Simular venta de productos
        List<String> codigosVenta = new ArrayList<>();
        codigosVenta.add("P001");
        codigosVenta.add("P002");
        List<Double> cantidadesVenta = new ArrayList<>();
        cantidadesVenta.add(5.0);
        cantidadesVenta.add(10.0);

        Venta venta = new Venta(1, "2024-03-21", "1234567890", "Efectivo", "Directa", codigosVenta, cantidadesVenta);
        double totalVenta = venta.calcularTotal(productos);
        System.out.println("Total a cobrar por la venta: " + totalVenta);
    }

    private static Producto buscarProductoPorCodigo(List<Producto> productos, String codigo) {
        for (Producto producto : productos) {
            if (producto.codigo.equals(codigo)) {
                return producto;
            }
        }
        return null;
    }
}
