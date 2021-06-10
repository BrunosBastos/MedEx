import {
    Avatar,
    Card,
    CardContent,
    Grid,
    Button,
    Typography,
    TextField,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle,
    Box
} from '@material-ui/core';
import { useState, useEffect } from 'react';
import { indigo } from '@material-ui/core/colors';
import EuroOutlinedIcon from '@material-ui/icons/EuroOutlined';
import useShopCartStore from 'src/stores/useShopCartStore';
import PurchaseService from 'src/services/purchaseService';
import { toast } from 'react-toastify';


const notifySuccess = (msg) => {
  toast.success(msg, {
    position: toast.POSITION.TOP_CENTER
    });
}

const notifyError = (msg) => {
  toast.error(msg, {
    position: toast.POSITION.TOP_CENTER
    });
}

const Purchase = () => {
  const [open, setOpen] = useState(false);
  const products = useShopCartStore(state => state.products)
  const [price, setPrice] = useState("0.00")

  const getTotalPrice = () => {
    let totalPrice = 0;
    for (let i = 0; i < products.length; i++) {
      totalPrice += products[i].price * products[i].quantity;
    }
    setPrice(totalPrice.toFixed(2))
  }

  useEffect(() => {
    getTotalPrice()
  }, [products])


  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const getPurchaseProducts = () => {
    let products_to_purchase = {};
    for (let i= 0; i < products.length; i++) {
        products_to_purchase[products[i].id] = products[i].quantity;
    }
    return products_to_purchase;
  }

  const handlePurchase = () => {
    const products_to_purchase = getPurchaseProducts();
    //@ts-ignore
    let lat: number = document.getElementById('latitude').value;
    //@ts-ignore
    let lon: number = document.getElementById('longitude').value;

    if (lat && lon) {
      PurchaseService.makePurchase(lat, lon, products_to_purchase)
      .then( (res) => {
        return res.json();
      })
      .then( (res) => {
        console.log(res)
        if(res.error) {
          notifyError("Failed making a purchase")
        } else{
          useShopCartStore.getState().removeAllProducts();
          // TODO redirect to the order details with res.id
          // navigate('/app/orders/' + res.id, { replace: true });
          notifySuccess("Successfully made a purchase")
        }
      })
      .catch( (error) => {
        notifyError("Failed making a purchase")
      })
      setOpen(false);
    }

  }

  return (
    <>
      <Card sx={{minWidth: 300}}>
        <CardContent>
          <Grid
            container
            sx={{ justifyContent: 'space-between' }}
            spacing={2}
          >
            <Grid item xs={5}>
              <Avatar
                sx={{
                  backgroundColor: indigo[600],
                  height: 56,
                  width: 56
                }}
              >
                <EuroOutlinedIcon />
              </Avatar>
            </Grid>
            <Grid item xs={7}>
              <Grid item xs={12}>
                <Box
                  sx={{
                    display: 'flex',
                    justifyContent: 'flex-end',
                  }}
                >
                  <Typography
                    color="textSecondary"
                    gutterBottom
                    variant="h6"
                  >
                    Total Price
                  </Typography>
                </Box>
              </Grid>
              <Grid item xs={12}>
                <Box
                  sx={{
                    display: 'flex',
                    justifyContent: 'flex-end',
                  }}
                >
                  <Typography
                    color="textPrimary"
                    variant="h3"
                  >
                    {price} €
                  </Typography>
                </Box>
              </Grid>
            </Grid>
          </Grid>
          <Grid
            container
            spacing={2}
            sx={{ justifyContent: 'space-between' }}
          >
            <Grid item xs={12}>
              <Box
                sx={{
                  display: 'flex',
                  justifyContent: 'flex-end',
                  pt: 2
                }}
              >
                <Button color="primary" variant="contained" onClick={handleClickOpen} disabled={products.length === 0}>
                  Purchase
                </Button>
              </Box>
            </Grid>
          </Grid>
        </CardContent>
      </Card>
      <Dialog open={open} onClose={handleClose} aria-labelledby="form-dialog-title">
        <DialogTitle id="form-dialog-title">Finalize Your Purchase</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Choose a Delivery Address
          </DialogContentText>
          <TextField
            autoFocus
            margin="dense"
            id="latitude"
            name="latitude"
            label="Latitude"
            type="number"
            required
            fullWidth
          />
          <TextField
            required
            margin="dense"
            id="longitude"
            name="longitude"
            label="Longitude"
            type="number"
            fullWidth
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose} color="primary">
            Cancel
          </Button>
          <Button onClick={handlePurchase} color="primary">
            Purchase
          </Button>
        </DialogActions>
      </Dialog>
    </>
  );
}
export default Purchase;