import React, { useEffect, useState } from 'react';
import {
  Card,
  CardContent,
  Typography,
  CardActions,
  Divider,
  Grid,
  CardHeader
} from '@mui/material';
import { useLocation, Link } from 'react-router-dom';

const ProductCard = ({ product }) => {
  if (!product) {
    return (
      <Card sx={{ maxWidth: 600, margin: '20px auto' }}>
        <CardHeader title="Product Details" />
        <CardContent>
          <Typography variant="h5" color="text.secondary" align="center">
            Product details are not available.
          </Typography>
        </CardContent>
      </Card>
    );
  }

  return (
    <Card sx={{ maxWidth: 900, margin: '30px auto', border: '3px solid #ccc' }}>
      <CardContent>
        {/* Título y Subcategoría protegidos contra nulos */}
        <Typography variant="h5" component="div" gutterBottom>
          {product.title || product.name}
        </Typography>

        {product.subCategory?.description && (
          <Typography variant="subtitle2" color="text.secondary" gutterBottom>
            Subcategory: {product.subCategory.description}
          </Typography>
        )}

        {/* Categoría protegida con ?. */}
        <Typography variant="body2" color="text.secondary">
          Category: {product.category?.title || 'N/A'} ({product.category?.metaTitle || 'N/A'})
        </Typography>

        <Divider sx={{ my: 2 }} />

        <Typography sx={{ fontWeight: 'bold' }} variant="subtitle1" gutterBottom>
          Summary:
        </Typography>
        <Typography variant="body1">
          {product.summary || 'No summary available.'}
        </Typography>

        <Divider sx={{ my: 2 }} />

        <Typography sx={{ fontWeight: 'bold' }} variant="subtitle1" gutterBottom>
          Content:
        </Typography>
        <Typography variant="body1">
          {product.content || 'No content available.'}
        </Typography>

        <Divider sx={{ my: 2 }} />

        <Grid container spacing={2}>
          <Grid item xs={6}>
            <Typography variant="body1" color="text.secondary">
              Created At: {product.createdAt ? new Date(product.createdAt).toLocaleDateString() : 'N/A'}
            </Typography>
          </Grid>
          <Grid item xs={6}>
            <Typography variant="body1" color="text.secondary">
              Updated At: {product.updatedAt ? new Date(product.updatedAt).toLocaleDateString() : 'N/A'}
            </Typography>
          </Grid>
        </Grid>
      </CardContent>

      <CardActions>
        <Link to="/products" style={{ textDecoration: 'none', color: '#1976d2', padding: '8px 16px' }}>
          Back to List
        </Link>
      </CardActions>
    </Card>
  );
};

const Product = () => {
  const location = useLocation();
  const [product, setProduct] = useState(null);

  useEffect(() => {
    // Uso de ?. para evitar error si location.state viene undefined
    if (location.state?.currentProduct) {
      setProduct(location.state.currentProduct);
    }
  }, [location.state]);

  return <ProductCard product={product} />;
};

export default Product;