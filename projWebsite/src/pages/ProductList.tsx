import { Helmet } from 'react-helmet';
import {useEffect, useState} from 'react'
import { 
  Box,
  Container, 
  Card,
  CardContent,
  Button,
  TextField,
  Grid,
  InputAdornment,
  SvgIcon
 } from '@material-ui/core';
import ProductListToolbar from 'src/components/product/ProductListToolbar';
import ProductCard from 'src/components/product/ProductCard';
import ProductService from "../services/productService";
import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import Select from '@material-ui/core/Select';
import Paginator from "src/components/paginator/Paginator";
import { Search as SearchIcon } from 'react-feather';
import useAuthStore from 'src/stores/useAuthStore';

const ProductList = () => {
  const [recent, setRecent] = useState("asc");
  const [recentOr, setRecentOr] = useState("asc");
  const [products,setProductsList] = useState([]);
  const [open, setOpen] = useState(false);
  const [page, setPage] = useState(0);
  const [prodName, setProdName] = useState("");
  const superUser = useAuthStore.getState().user.superUser

  const handleChange = (event) => {
    setRecent(event.target.value)
  };

  const handleClose = () => {
    setOpen(false);
  };

  const handleOpen = () => {
    setOpen(true);
  };

  const updateValues = () => {
    //@ts-ignore
    setProdName(document.getElementById("productName").value);
    setRecentOr(recent); 
  }

  useEffect( () => {
    ProductService.getAllProducts(prodName, page, recentOr == 'desc')
      .then( (res) => {
        if (res.status == 200)
          return res.json()
        return null
      })
      .then( (res) => {
        console.log(res)
        if (res)
          setProductsList(res)
      })
      .catch( () => {
        console.log("Something went wrong")
      })
  }, [page, prodName, recentOr])

  return(
  <>
    <Helmet>
      <title>Products</title>
    </Helmet>
    <Box
      sx={{
        backgroundColor: 'background.default',
        minHeight: '100%',
        py: 3
      }}
    >
      <Container maxWidth={false}>
        <Card>
          <CardContent>
            <Grid
              container
              xs={12}
            >
              <Grid
                item
                sm={6}
                xs={12}
                style={{textAlign: 'center', margin: 'auto'}}
              > 
                <TextField
                  fullWidth
                  id="productName"
                  InputProps={{
                  startAdornment: (
                      <InputAdornment position="start">
                      <SvgIcon
                          fontSize="small"
                          color="action"
                      >
                          <SearchIcon />
                      </SvgIcon>
                      </InputAdornment>
                  )
                  }}
                  placeholder="Search By Product Name"
                  variant="outlined"
                />
          </Grid>
              <Grid
                  item
                  sm={3}
                  xs={12}
                  style={{textAlign: 'center', margin: 'auto'}}
              > 
                <InputLabel id="demo-controlled-open-select-label">Sort Mode</InputLabel>
                <Select
                labelId="demo-controlled-open-select-label"
                id="demo-controlled-open-select"
                open={open}
                onClose={handleClose}
                onOpen={handleOpen}
                value={recent}
                onChange={handleChange}
                >
                <MenuItem value={"asc"}>Ascending</MenuItem>
                <MenuItem value={"desc"}>Descending</MenuItem>
                </Select>
              </Grid>
              <Grid
                item
                sm={3}
                xs={12}
                style={{textAlign: 'center', margin: 'auto'}}
              > 
                <Button
                    color="primary"
                    variant="contained"
                    size="large"
                    onClick= {() => updateValues()}
                >
                  Search
                </Button>
              </Grid>
            </Grid>
          </CardContent>
        </Card>
        <Box sx={{ pt: 3 }}>
          <Grid
            container
            spacing={3}
          >
            {products && 
              products?.map((prod, index) => {
                return (
                  <Grid
                    item
                    key={prod.id}
                    lg={4}
                    md={6}
                    xs={12}
                  >
                    <ProductCard product={prod} superUser={superUser}/>
                  </Grid>
                )
              })
            }
            
          </Grid>
        </Box>
        <Box
          sx={{
            display: 'flex',
            justifyContent: 'center',
            pt: 3
          }}
        >
          <Paginator hasNext={products.length == 10} page={page} changePage={(page) => setPage(page)}/>
        </Box>
      </Container>
    </Box>
  </>
);
}

export default ProductList;
