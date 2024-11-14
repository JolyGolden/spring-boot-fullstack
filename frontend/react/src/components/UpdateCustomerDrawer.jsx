import {
    Button,
    Drawer,
    DrawerBody,
    DrawerCloseButton, DrawerContent,
    DrawerFooter,
    DrawerHeader,
    DrawerOverlay, Input, useDisclosure
} from "@chakra-ui/react";
import CreateCustomerForm from "./CreateCustomerForm.jsx";
import UpdateCustomerForm from "./UpdateCustomerForm.jsx";
import React from "react";




const AddIcon = () => "+";
const CloseIcon = () => "x";



const UpdateCustomerDrawer = ({fetchCustomers, initialValues, customerId}) => {
    const { isOpen, onOpen, onClose } = useDisclosure()

    return <>

        <Button

            bg={'gray.400'}
            color={'white'}
            rounded={'full'}
            _hover={{
                tranform: 'translate(-2px)',
                boxShadow:'lg'
            }
            }
            _focus={{
                bg:"gray.400"
            }}
            onClick={onOpen}
        >
            Edit
        </Button>
        <Drawer isOpen={isOpen} onClose={onClose} size={"xl"}>
            <DrawerOverlay />
            <
                DrawerContent>
                <DrawerCloseButton />
                <DrawerHeader>Edit a customer</DrawerHeader>

                <DrawerBody>
                    <UpdateCustomerForm
                        fetchCusomers={fetchCustomers}
                        initialValues={initialValues}
                        customerId={customerId}
                    />
                </DrawerBody>

                <DrawerFooter>
                    <Button
                        leftIcon={<CloseIcon/>}
                        colorScheme={"linkedin"}
                        onClick={onClose}
                    >
                        Close
                    </Button>
                </DrawerFooter>
            </DrawerContent>
        </Drawer>
    </>

}

export default UpdateCustomerDrawer;

